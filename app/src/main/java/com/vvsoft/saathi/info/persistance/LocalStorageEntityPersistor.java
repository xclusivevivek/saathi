package com.vvsoft.saathi.info.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.entity.dao.exception.InvalidEntityFile;
import com.vvsoft.saathi.entity.dao.exception.StoragePathInvalidException;
import com.vvsoft.saathi.entity.persistance.EntityPersistor;
import com.vvsoft.saathi.info.schema.model.Copyable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalStorageEntityPersistor<T extends NamedEntity & Copyable<T>> implements EntityPersistor<T> {
    private final Path storagePath;
    private final ObjectMapper jsonMapper;
    private final String entityName;

    public LocalStorageEntityPersistor(String storagePath, ObjectMapper jsonMapper, String entityName) {
        this.storagePath = Path.of(storagePath);
        this.jsonMapper = jsonMapper;
        this.entityName = entityName;
    }

    @Override
    public void initialize() {
        if(!Files.exists(this.storagePath)) {
            try {
                Files.createDirectories(this.storagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Collection<T> getAll() {
        try(Stream<Path> files = Files.walk(storagePath,2)){
            return files.filter(file -> file.toAbsolutePath().toString().endsWith("." + entityName))
                    .map(file -> {
                        try {
                            return jsonMapper.readValue(file.toFile(), new TypeReference<T>() {});
                        } catch (IOException e) {
                            throw new InvalidEntityFile(String.format("Error while opening file : %s", file.getFileName()), e);
                        }
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error while loading cache",e);
        }
    }

    @Override
    public void persistEntity(T newEntity) {
        try {
            byte[] serializedEntity;
            serializedEntity = jsonMapper.writeValueAsBytes(newEntity);
            Path createdFile = Files.createFile(getEntityFilePath(newEntity.getId()));
            Files.write(createdFile,serializedEntity, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error while creating file",e);
        }
    }

    @Override
    public void updateEntity(T newEntity) {
        Path entityFilePath = getEntityFilePath(newEntity.getId());
        try {
            jsonMapper.writeValue(entityFilePath.toFile(), newEntity);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error in updating file " + entityFilePath.getFileName());
        }
    }

    @Override
    public void deleteEntity(T entity) {
        Path entityFilePath = getEntityFilePath(entity.getId());
        try {
            Files.delete(entityFilePath);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error in deleting file " + entityFilePath.getFileName());
        }
    }

    private Path getEntityFilePath(String newId) {
        return storagePath.resolve(newId + "." + entityName);
    }
}
