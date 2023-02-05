package com.vvsoft.saathi.info.schema.controller;

import com.vvsoft.saathi.info.schema.SchemaRepository;
import com.vvsoft.saathi.info.schema.dto.InfoSchemaDto;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/schema")
public class SchemaController {
    private final SchemaRepository schemaRepository;
    @Autowired
    public SchemaController(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @GetMapping()
    public @ResponseBody List<InfoSchema> getAllSchema(){
        return schemaRepository.findAll();
    }

    @GetMapping(path = "/get/{name}")
    public @ResponseBody ResponseEntity<InfoSchema> getSchema(@PathVariable("name") String name){
        Optional<InfoSchema> infoSchema = schemaRepository.find(name);
        return infoSchema.map(schema -> new ResponseEntity<>(schema, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<InfoSchemaDto> createSchema(@RequestBody InfoSchemaDto infoSchemaDto){
        InfoSchema infoSchema = schemaRepository.create(infoSchemaDto.toSchema());
        InfoSchemaDto response = InfoSchemaDto.fromSchema(infoSchema);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<InfoSchemaDto> updateSchema(@RequestBody InfoSchemaDto infoSchemaDto){
        InfoSchema infoSchema = schemaRepository.update(infoSchemaDto.toSchema());
        InfoSchemaDto response = InfoSchemaDto.fromSchema(infoSchema);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/{name}")
    public ResponseEntity<Void> updateSchema(@PathVariable String name){
        schemaRepository.delete(name);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
