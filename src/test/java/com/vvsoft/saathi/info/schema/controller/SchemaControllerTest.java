package com.vvsoft.saathi.info.schema.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchemaControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;

    @Test
    @Disabled("Disabled as this is dummy test")
    void getAllSchemaEndpointReturnsEmptyList(){
        String url = String.format("http://localhost:%d/schema/list",port);
        assertEquals(List.of(), restTemplate.getForObject(url, List.class));
    }

}