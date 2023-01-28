package com.vvsoft.saathi.info.schema.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.info.schema.dao.exception.InvalidSchemaFile;
import com.vvsoft.saathi.info.schema.dao.exception.SchemaAlreadyExistsException;
import com.vvsoft.saathi.info.schema.dao.exception.StoragePathInvalidException;
import com.vvsoft.saathi.info.schema.model.InfoSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaFileDao<T extends InfoSchema> implements InfoSchemaDao<T>{
    public static final String SCHEMA_SUFFIX = ".schema";
    private final Path storagePath;
    private final ObjectMapper jsonMapper;
    private final List<InfoSchema> cache = new ArrayList<>();

    public SchemaFileDao(String storagePath) throws IOException {
        this.storagePath = Path.of(storagePath);
        if(!Files.exists(this.storagePath))
            Files.createDirectories(this.storagePath);
        jsonMapper = new ObjectMapper();
        cache.addAll(getAllSchemas(this.storagePath));
    }

    @Override
    public T create(T schema) throws SchemaAlreadyExistsException {
        if(getSchemaByName(schema.getName()).isPresent()){
            throw new SchemaAlreadyExistsException(schema.getName());
        }
        String newId = UUID.randomUUID().toString();
        try {
            Path createdFile = createSchemaFile(newId);
            InfoSchema newSchema = schema.copy();
            newSchema.setId(newId);
            schema.setId(newId);
            jsonMapper.writeValue(createdFile.toFile(),newSchema);
            cache.add(newSchema);
        } catch (IOException e) {
            throw new RuntimeException("Error while creating file",e);
        }

        return schema;
    }



    @Override
    public Optional<T> read(String id) {
        return cache.stream().filter(infoSchema -> infoSchema.getId().equals(id)).findAny().map(schema -> (T)schema.copy());
    }

    @Override
    public void update(T schema) {
        Optional<InfoSchema> existingSchema = getSchemaByName(schema.getName());
        if(existingSchema.isPresent()){
            try {
                schema.setId(existingSchema.get().getId());
                Path schemaFilePath = storagePath.resolve(existingSchema.get().getId() + SCHEMA_SUFFIX);
                jsonMapper.writeValue(schemaFilePath.toFile(),schema);
                cache.remove(existingSchema.get());
                cache.add(schema.copy());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(String id) {

    }

    private Optional<InfoSchema> getSchemaByName(String name){
        return cache.stream().filter(infoSchema -> infoSchema.getName().equals(name)).findFirst();
    }

    private List<InfoSchema> getAllSchemas(Path storagePath) {
        try(Stream<Path> files = Files.walk(storagePath)){
            return files.filter(file -> file.endsWith(SCHEMA_SUFFIX))
                    .map(file -> {
                        try {
                            return jsonMapper.readValue(file.toFile(), InfoSchema.class);
                        } catch (IOException e) {
                            throw new InvalidSchemaFile(String.format("Error while opening file : %s", file.getFileName()), e);
                        }
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error while loading cache",e);
        }
    }

    private Path createSchemaFile(String newId) throws IOException {
        return Files.createFile(storagePath.resolve(newId + SCHEMA_SUFFIX));
    }
}
