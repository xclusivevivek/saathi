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
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/schema")
public class SchemaController {
    private final SchemaRepository schemaRepository;
    @Autowired
    public SchemaController(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @GetMapping()
    public List<InfoSchemaDto> getAllSchema(){
        return schemaRepository.findAll().stream().map(InfoSchemaDto::fromSchema).collect(Collectors.toList());
    }

    @GetMapping(path = "/get/{name}")
    public @ResponseBody ResponseEntity<InfoSchema> getSchema(@PathVariable("name") String name){
        Optional<InfoSchema> infoSchema = schemaRepository.find(name);
        return infoSchema.map(schema -> new ResponseEntity<>(schema, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public InfoSchemaDto createSchema(@RequestBody InfoSchemaDto infoSchemaDto){
        InfoSchema infoSchema = schemaRepository.create(infoSchemaDto.toSchema());
        return InfoSchemaDto.fromSchema(infoSchema);
    }

    @PostMapping(path = "/update")
    @ResponseStatus(HttpStatus.CREATED)
    public InfoSchemaDto updateSchema(@RequestBody InfoSchemaDto infoSchemaDto){
        InfoSchema infoSchema = schemaRepository.update(infoSchemaDto.toSchema());
        return InfoSchemaDto.fromSchema(infoSchema);
    }

    @DeleteMapping(path = "/delete/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void updateSchema(@PathVariable String name){
        schemaRepository.delete(name);
    }
}
