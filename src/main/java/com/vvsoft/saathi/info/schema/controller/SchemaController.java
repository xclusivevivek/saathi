package com.vvsoft.saathi.info.schema.controller;

import com.vvsoft.saathi.info.schema.SchemaService;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/schema")
public class SchemaController {
    private final SchemaService schemaService;
    @Autowired
    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping(path = "/list")
    public @ResponseBody List<InfoSchema> getAllSchema(){
        return schemaService.getAll();
    }
}
