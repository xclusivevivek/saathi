package com.vvsoft.saathi.info.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.entity.dao.exception.InvalidEntityFile;
import com.vvsoft.saathi.entity.dao.exception.StoragePathInvalidException;
import com.vvsoft.saathi.entity.persistance.EntityPersistor;
import com.vvsoft.saathi.entity.persistance.StorageFileSystem;
import com.vvsoft.saathi.info.schema.model.Copyable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBasedEntityPersistor<T extends NamedEntity & Copyable<T>> implements EntityPersistor<T> {
    private final Path storagePath;
    private final ObjectMapper jsonMapper;
    private final String entityName;
    private final StorageFileSystem storageFileSystem;

    public FileBasedEntityPersistor(String storagePath,
                                    ObjectMapper jsonMapper,
                                    String entityName,
                                    StorageFileSystem storageFileSystem
                                    ) {
        this.storagePath = Path.of(storagePath);
        this.jsonMapper = jsonMapper;
        this.entityName = entityName;
        this.storageFileSystem = storageFileSystem;
    }

    @Override
    public void initialize() throws IOException {
        if(!storageFileSystem.doesFolderExists(this.storagePath)) {
            try {
                storageFileSystem.createFolders(this.storagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Collection<T> getAll() {
        try(Stream<Path> files = storageFileSystem.walk(storagePath)){
            return files.filter(file -> file.toAbsolutePath().toString().endsWith("." + entityName))
                    .map(file -> {
                        try {
                            return jsonMapper.readValue(file.toFile(), new TypeReference<T>() {});
                        } catch (IOException e) {
                            throw new InvalidEntityFile(String.format("Error while opening file : %s", file.getFileName()), e);
                        }
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void persistEntity(T newEntity) {
        try {
            byte[] serializedEntity;
            serializedEntity = jsonMapper.writeValueAsBytes(newEntity);
            storageFileSystem.createFile(getEntityFilePath(newEntity.getId()), serializedEntity);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error while creating file",e);
        }
    }

    @Override
    public void updateEntity(T newEntity) {
        Path entityFilePath = getEntityFilePath(newEntity.getId());
        try {
            byte[] serializedEntity = jsonMapper.writeValueAsBytes(newEntity);
            storageFileSystem.overwriteFile(entityFilePath, serializedEntity);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error in updating file " + entityFilePath.getFileName());
        }
    }

    @Override
    public void deleteEntity(T entity) {
        Path entityFilePath = getEntityFilePath(entity.getId());
        try {
            storageFileSystem.deleteFile(entityFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getEntityFilePath(String newId) {
        return storagePath.resolve(newId + "." + entityName);
    }
}
