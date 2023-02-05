package com.vvsoft.saathi.info.schema.controller;

import com.vvsoft.saathi.info.schema.SchemaRepository;
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
    private final SchemaRepository schemaRepository;
    @Autowired
    public SchemaController(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @GetMapping(path = "/list")
    public @ResponseBody List<InfoSchema> getAllSchema(){
        return schemaRepository.findAll();
    }
}
