package com.vvsoft.saathi.info.schema.controller;

import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import com.vvsoft.saathi.test.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SchemaControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;

    @Value("${app.schema.storage.path}")
    private String storagePath;

    @AfterEach
    void cleanUpTest() throws IOException {
        StorageUtil.clearDirectory(storagePath);
    }
    @Test
    void testSchemaLifecycle(){

        //create schema
        InfoSchemaDto dto = buildInfoSchema("address");
        ResponseEntity<InfoSchemaDto> dtoResponse = makePostRequest(getCreateSchemaUrl(), dto);
        assertEquals(HttpStatus.CREATED, dtoResponse.getStatusCode());
        InfoSchemaDto createdDto = Objects.requireNonNull(dtoResponse.getBody());
        assertEquals("address", createdDto.getName());

        //getSchema
        dtoResponse = makeGetRequest(getRetrieveSchemaUrl(createdDto));
        assertEquals(HttpStatus.OK, dtoResponse.getStatusCode());
        assertEquals("address", dtoResponse.getBody().getName());

        //updateSchema
        createdDto.getFields().add(new SimpleField("area",FieldType.TEXT));
        dtoResponse = restTemplate.postForEntity(getUpdateSchemaUrl(), createdDto,InfoSchemaDto.class);
        assertEquals(HttpStatus.CREATED, dtoResponse.getStatusCode());
        assertEquals("address", dtoResponse.getBody().getName());


        List<InfoSchemaDto> schemas = restTemplate.getForObject(getUrlForFindAllSchema(), List.class);
        assertTrue(schemas.size() > 0);
    }

    @Test
    void testCreateError(){
        String url = getCreateSchemaUrl();
        InfoSchemaDto infoSchemaDto = buildInfoSchema("createError");
        ResponseEntity<InfoSchemaDto> response = makePostRequest(url, infoSchemaDto);
        ResponseEntity<?> secondResponse = makePostRequest(url, infoSchemaDto);
        assertEquals(HttpStatus.CONFLICT,secondResponse.getStatusCode());
    }

    @Test
    void testUpdateError(){
        String url = getCreateSchemaUrl();
        InfoSchemaDto infoSchemaDto = buildInfoSchema("updateError");
        ResponseEntity<InfoSchemaDto> response = makePostRequest(url, infoSchemaDto);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        infoSchemaDto.setName("dummyUpdateError");
        ResponseEntity<?> secondResponse = makePostRequest(getUpdateSchemaUrl(), infoSchemaDto);
        assertEquals(HttpStatus.NOT_FOUND,secondResponse.getStatusCode());
    }

    private ResponseEntity<InfoSchemaDto> makeGetRequest(String url) {
        return restTemplate.getForEntity(url, InfoSchemaDto.class);
    }

    private ResponseEntity<InfoSchemaDto> makePostRequest(String url, InfoSchemaDto dto) {
        ResponseEntity<InfoSchemaDto> dtoResponse = restTemplate.postForEntity(url, dto, InfoSchemaDto.class);
        return dtoResponse;
    }

    private static InfoSchemaDto buildInfoSchema(String schemaName) {
        InfoSchemaDto dto = InfoSchemaDto.builder().name(schemaName).field(new SimpleField("flatNo", FieldType.NUMBER)).build();
        return dto;
    }

    private String getDeleteSchemaUrl(InfoSchemaDto createdDto) {
        return String.format("http://localhost:%d/schema/delete/%s", port, createdDto.getName());
    }

    private String getUpdateSchemaUrl() {
        return String.format("http://localhost:%d/schema/update", port);
    }

    private String getRetrieveSchemaUrl(InfoSchemaDto createdDto) {
        return String.format("http://localhost:%d/schema/get/%s", port, createdDto.getName());
    }

    private String getCreateSchemaUrl() {
        return String.format("http://localhost:%d/schema/create", port);
    }

    private String getUrlForFindAllSchema() {
        return String.format("http://localhost:%d/schema", port);
    }


}