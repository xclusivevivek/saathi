package com.vvsoft.saathi.info.schema.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.info.schema.model.InfoSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchemaFileDao<T extends InfoSchema> implements InfoSchemaDao<T>{
    private final String storagePath;
    private final ObjectMapper jsonMapper;

    public SchemaFileDao(String storagePath) throws IOException {
        this.storagePath = storagePath;
        Path storage= Path.of(storagePath);
        if(!Files.exists(storage))
            Files.createDirectories(storage);
        jsonMapper = new ObjectMapper();
    }

    @Override
    public void create(T schema) {
        Path savePath = Path.of(storagePath).resolve(schema.getId() + ".schema");
        try {
            if(!Files.exists(savePath))
                Files.createFile(savePath);
            jsonMapper.writeValue(savePath.toFile(),schema);
        } catch (IOException e) {
            //TODO Send Specific exception
            throw new RuntimeException(String.format("Unable To Save File %s",savePath.getFileName()),e);
        }
    }

    @Override
    public T read(long id) {
        return null;
    }

    @Override
    public void update(T schema) {

    }

    @Override
    public void delete(long id) {

    }
}
