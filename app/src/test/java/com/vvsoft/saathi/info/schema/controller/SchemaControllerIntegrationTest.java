package com.vvsoft.saathi.info.schema.controller;

import com.vvsoft.saathi.MockConfiguration;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import com.vvsoft.saathi.info.schema.presentation.InfoSchemaDto;
import com.vvsoft.saathi.test.util.SchemaRestTestClient;
import com.vvsoft.saathi.test.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {MockConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SchemaControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private StorageUtil storageUtil;

    private SchemaRestTestClient schemaRestClient;

    @BeforeAll
    public void init(){
        schemaRestClient = new SchemaRestTestClient(restTemplate,port);
    }

    @AfterEach
    void cleanUpTest() throws IOException {
        storageUtil.clearSchemaStoragePath();
    }
    @Test
    void testSchemaLifecycle(){

        //create schema
        ResponseEntity<InfoSchemaDto> dtoResponse = schemaRestClient.createTestSchema("address");
        assertEquals(HttpStatus.CREATED, dtoResponse.getStatusCode());
        InfoSchemaDto createdDto = Objects.requireNonNull(dtoResponse.getBody());
        assertEquals("address", createdDto.getName());

        //getSchema
        dtoResponse = schemaRestClient.getSchema(createdDto.getName());
        assertEquals(HttpStatus.OK, dtoResponse.getStatusCode());
        assertEquals("address", dtoResponse.getBody().getName());

        //updateSchema
        createdDto.getFields().add(new SimpleField("area",FieldType.TEXT));
        dtoResponse = schemaRestClient.updateTestSchema(createdDto);
        assertEquals(HttpStatus.CREATED, dtoResponse.getStatusCode());
        assertEquals("address", dtoResponse.getBody().getName());


        List<InfoSchemaDto> schemas = schemaRestClient.getAll();
        assertTrue(schemas.size() > 0);
    }

    @Test
    void testCreateError(){
        schemaRestClient.createTestSchema("createError");
        ResponseEntity<InfoSchemaDto> secondResponse = schemaRestClient.createTestSchema("createError");
        assertEquals(HttpStatus.CONFLICT,secondResponse.getStatusCode());
    }

    @Test
    void testUpdateError(){
        ResponseEntity<InfoSchemaDto> response = schemaRestClient.createTestSchema("updateError");
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        InfoSchemaDto infoSchemaDto = response.getBody();
        infoSchemaDto.setName("dummyUpdateError");
        ResponseEntity<?> secondResponse =
                schemaRestClient.updateTestSchema(infoSchemaDto);
        assertEquals(HttpStatus.BAD_REQUEST,secondResponse.getStatusCode());
    }

    private String getDeleteSchemaUrl(InfoSchemaDto createdDto) {
        return String.format("http://localhost:%d/schema/delete/%s", port, createdDto.getName());
    }


}