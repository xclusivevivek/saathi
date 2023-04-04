package com.vvsoft.saathi.info.record.controller;

import com.vvsoft.saathi.error.rest.ApiError;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.test.util.SchemaRestTestClient;
import com.vvsoft.saathi.test.util.StorageUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfoRecordControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private String baseUrl;
    private SchemaRestTestClient schemaRestTestClient;

    @Value("${app.record.storage.path}")
    private String recordStoragePath;

    @Value("${app.schema.storage.path}")
    private String schemaStoragePath;

    @BeforeAll
    void init(){
        baseUrl = String.format("http://localhost:%s",port);
        schemaRestTestClient = new SchemaRestTestClient(restTemplate, port);
    }

    @AfterEach
    void cleanUpTest() throws IOException {
        StorageUtil.clearDirectory(recordStoragePath);
        StorageUtil.clearDirectory(schemaStoragePath);
    }

    @Test
    void canCreateInfoRecord(){
        String schemaName = "FooSchema";
        ResponseEntity<InfoSchemaDto> response = schemaRestTestClient.createTestSchema(schemaName);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        ResponseEntity<InfoRecordDto> recordResponse = restTemplate.postForEntity(baseUrl + "/inforecord/create",
                InfoRecordDto.builder().schemaName(schemaName).name("BarRecord").values(Map.of("flatNo", "123")).build(), InfoRecordDto.class);
        assertEquals(HttpStatus.CREATED,recordResponse.getStatusCode());
    }

    @Test
    void whenMissingSchemaIsSpecified_CreateShouldReturnBadRequestError(){
        String schemaName = "MissingSchema";
        ResponseEntity<ApiError> recordResponse = restTemplate.postForEntity(baseUrl + "/inforecord/create",
                InfoRecordDto.builder().schemaName(schemaName).name("BadRecord").values(Map.of("flatNo", "123")).build(), ApiError.class);
        assertEquals(HttpStatus.BAD_REQUEST,recordResponse.getStatusCode());
        assertTrue(Objects.requireNonNull(recordResponse.getBody()).getDebugDetail().contains("EntityNotFoundException"));
    }

    @Test
    void whenInvalidFieldIsSpecified_CreateShouldBadRequestError(){
        String schemaName = "MissingField";
        ResponseEntity<InfoSchemaDto> response = schemaRestTestClient.createTestSchema(schemaName);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        ResponseEntity<ApiError> recordResponse = restTemplate.postForEntity(baseUrl + "/inforecord/create",
                InfoRecordDto.builder().schemaName(schemaName).name("BadRecord").values(Map.of("InvalidField", "123")).build(), ApiError.class);
        System.out.println(recordResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,recordResponse.getStatusCode());
        assertTrue(recordResponse.getBody().getDebugDetail().contains("FieldNotFoundInSchemaException"));
    }

}