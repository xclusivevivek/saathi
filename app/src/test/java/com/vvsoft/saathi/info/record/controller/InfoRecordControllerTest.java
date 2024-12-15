package com.vvsoft.saathi.info.record.controller;

import com.vvsoft.saathi.error.rest.ApiError;
import com.vvsoft.saathi.info.record.dto.InfoRecordDetailDto;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.test.util.SchemaRestTestClient;
import com.vvsoft.saathi.test.util.StorageUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
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

    @Autowired
    private StorageUtil storageUtil;
    private String createRecordUrl;
    private String findRecordUrl;
    private String updateRecordUrl;
    private String deleteUrl;

    @BeforeAll
    void init(){
        baseUrl = String.format("http://localhost:%s/inforecord",port);
        schemaRestTestClient = new SchemaRestTestClient(restTemplate, port);
        createRecordUrl = baseUrl + "/create";
        findRecordUrl = baseUrl + "/get?name={name}";
        updateRecordUrl = baseUrl + "/update";
        deleteUrl = baseUrl + "/delete?name={name}";
    }

    @AfterEach
    void cleanUpTest() throws IOException {
        storageUtil.clearSchemaStoragePath();
        storageUtil.clearRecordStoragePath();
    }

    @Test
    void canCreateInfoRecord(){
        String schemaName = "FooSchema";
        ResponseEntity<InfoRecordDto> recordResponse = makeCreateRequest(schemaName, "BarRecord");
        assertEquals(HttpStatus.CREATED,recordResponse.getStatusCode());
    }

    @Test
    void whenMissingSchemaIsSpecified_CreateShouldReturnBadRequestError(){
        String schemaName = "MissingSchema";
        ResponseEntity<ApiError> recordResponse = restTemplate.postForEntity(createRecordUrl,
                InfoRecordDto.builder().schemaName(schemaName).name("BadRecord").values(Map.of("flatNo", "123")).build(), ApiError.class);
        assertEquals(HttpStatus.BAD_REQUEST,recordResponse.getStatusCode());
        assertTrue(Objects.requireNonNull(recordResponse.getBody()).getDebugDetail().contains("EntityNotFoundException"));
    }

    @Test
    void whenInvalidFieldIsSpecified_CreateShouldBadRequestError(){
        String schemaName = "MissingField";
        ResponseEntity<InfoSchemaDto> response = schemaRestTestClient.createTestSchema(schemaName);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        ResponseEntity<ApiError> recordResponse = restTemplate.postForEntity(createRecordUrl,
                InfoRecordDto.builder().schemaName(schemaName).name("BadRecord").values(Map.of("InvalidField", "123")).build(), ApiError.class);
        System.out.println(recordResponse.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,recordResponse.getStatusCode());
        assertTrue(recordResponse.getBody().getDebugDetail().contains("FieldNotFoundInSchemaException"));
    }

    @Test
    void canGetAllRecordValues(){
        String schemaName = "AllRecordsSchema";
        ResponseEntity<InfoSchemaDto> response = schemaRestTestClient.createTestSchema(schemaName);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        String recordName = "AllRecord";
        ResponseEntity<InfoRecordDto> recordResponse = restTemplate.postForEntity(createRecordUrl,
                InfoRecordDto.builder().schemaName(schemaName).name(recordName).values(Map.of("flatNo", "123")).build(), InfoRecordDto.class);
        assertEquals(HttpStatus.CREATED,recordResponse.getStatusCode());

        ResponseEntity<List<InfoRecordDetailDto>> recordList = restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertTrue(recordList.getBody().stream().anyMatch(rec -> rec.getName().equals(recordName)));
        assertTrue(recordList.getBody().stream().anyMatch(rec -> rec.getSchema().getName().equals(schemaName)));
        assertTrue(recordList.getBody().stream().anyMatch(rec -> rec.getValues().containsKey("flatNo")));
        assertTrue(recordList.getBody().stream().anyMatch(rec -> rec.getValues().containsValue(123)));
    }

    @Test
    void canGetExistingRecordByName(){
        String schemaName = "findSchema";
        String recordName = "findName";
        ResponseEntity<InfoRecordDto> createResponse = makeCreateRequest(schemaName, recordName);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        ResponseEntity<InfoRecordDetailDto> findResponse = makeReadRequest(recordName);
        assertEquals(HttpStatus.OK,findResponse.getStatusCode());
        InfoRecordDetailDto infoRecordDto = findResponse.getBody();
        assertEquals(Objects.requireNonNull(infoRecordDto).getName(), recordName);
        assertEquals(Objects.requireNonNull(infoRecordDto).getSchema().getName(), schemaName);
    }

    @Test
    void whenRecordIsMissing_getRequestReturnsEmpty(){
        ResponseEntity<InfoRecordDetailDto> response = makeReadRequest("dummy");
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    void canUpdateRecord(){
        String updateField = "flatNo";
        ResponseEntity<InfoRecordDto> createResponse = makeCreateRequest("UpdateSchema", "UpdateRecord");
        assertEquals(HttpStatus.CREATED,createResponse.getStatusCode());
        InfoRecordDto originalRecord = createResponse.getBody();
        Objects.requireNonNull(originalRecord).setValue(updateField,"346");
        ResponseEntity<InfoRecordDetailDto> response = makeUpdateRequest(originalRecord);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        InfoRecordDetailDto recordDetailDto = response.getBody();
        Object value = Objects.requireNonNull(recordDetailDto).getValues().get(updateField);
        assertEquals(346,value);
    }

    @Test
    void whenRecordIsMissing_updateRequestReturnsBadRequest(){
        ResponseEntity<InfoRecordDetailDto> response = makeUpdateRequest(InfoRecordDto.builder().name("dummy").build());
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    void canDeleteRecord(){
        String recordName = "DeleteRecord";
        ResponseEntity<InfoRecordDto> createResponse = makeCreateRequest("DeleteSchema", recordName);
        assertEquals(HttpStatus.CREATED,createResponse.getStatusCode());
        ResponseEntity<Object> response = makeDeleteRequest(recordName);
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }

    @Test
    void whenRecordIsMissing_deleteRequestReturnsBadRequest(){
        ResponseEntity<Object> response = makeDeleteRequest("Dummy");
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private ResponseEntity<Object> makeDeleteRequest(String recordName) {
        ResponseEntity<Object> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, Object.class, recordName);
        return response;
    }

    private ResponseEntity<InfoRecordDetailDto> makeUpdateRequest(InfoRecordDto recordToUpdate) {
        ResponseEntity<InfoRecordDetailDto> response = restTemplate.postForEntity(updateRecordUrl, recordToUpdate, InfoRecordDetailDto.class);
        return response;
    }

    private ResponseEntity<InfoRecordDto> makeCreateRequest(String schemaName, String recordName) {
        ResponseEntity<InfoSchemaDto> response = schemaRestTestClient.createTestSchema(schemaName);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        return restTemplate.postForEntity(createRecordUrl,
                InfoRecordDto.builder().schemaName(schemaName).name(recordName).values(Map.of("flatNo", "123")).build(), InfoRecordDto.class);
    }

    private ResponseEntity<InfoRecordDetailDto> makeReadRequest(String recordName) {
        ResponseEntity<InfoRecordDetailDto> findResponse = restTemplate.getForEntity(findRecordUrl, InfoRecordDetailDto.class, Map.of("name", recordName));
        return findResponse;
    }

}