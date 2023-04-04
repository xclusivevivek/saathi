package com.vvsoft.saathi.test.util;

import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.info.schema.model.field.FieldType;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class SchemaRestTestClient {
    private final TestRestTemplate restTemplate;
    private final int port;

    public SchemaRestTestClient(TestRestTemplate restTemplate, int port) {
        this.restTemplate = restTemplate;
        this.port = port;
    }

    public ResponseEntity<InfoSchemaDto> createTestSchema(String schemaName) {
        InfoSchemaDto dto = buildInfoSchema(schemaName);
        return makePostRequest(getCreateSchemaUrl(), dto);
    }

    public ResponseEntity<InfoSchemaDto> updateTestSchema(InfoSchemaDto dto) {
        return makePostRequest(getUpdateSchemaUrl(), dto);
    }

    public ResponseEntity<InfoSchemaDto> getSchema(String name) {
        return makeGetRequest(getRetrieveSchemaUrl(name));
    }

    public List<InfoSchemaDto> getAll() {
        return restTemplate.getForObject(getUrlForFindAllSchema(), List.class);
    }

    private static InfoSchemaDto buildInfoSchema(String schemaName) {
        return InfoSchemaDto.builder().name(schemaName).field(new SimpleField("flatNo", FieldType.NUMBER)).build();
    }

    private ResponseEntity<InfoSchemaDto> makePostRequest(String url, InfoSchemaDto dto) {
        return restTemplate.postForEntity(url, dto, InfoSchemaDto.class);
    }

    private String getCreateSchemaUrl() {
        return String.format("http://localhost:%d/schema/create", port);
    }

    private String getUpdateSchemaUrl() {
        return String.format("http://localhost:%d/schema/update", port);
    }

    private String getRetrieveSchemaUrl(String name) {
        return String.format("http://localhost:%d/schema/get/%s", port, name);
    }

    private ResponseEntity<InfoSchemaDto> makeGetRequest(String url) {
        return restTemplate.getForEntity(url, InfoSchemaDto.class);
    }

    private String getUrlForFindAllSchema() {
        return String.format("http://localhost:%d/schema", port);
    }
}
