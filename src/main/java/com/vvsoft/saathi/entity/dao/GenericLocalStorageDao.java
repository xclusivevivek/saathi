package com.vvsoft.saathi.entity.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.entity.dao.exception.InvalidEntityFile;
import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.entity.dao.exception.StoragePathInvalidException;
import com.vvsoft.saathi.info.schema.model.Copyable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenericLocalStorageDao<T extends NamedEntity & Copyable<T>> implements GenericDao<T>{
    public final String entityName;
    private final Path storagePath;
    private final ObjectMapper jsonMapper;
    private final List<T> cache = new ArrayList<>();

    public GenericLocalStorageDao(String storagePath, String entityName) throws IOException {
        this.storagePath = Path.of(storagePath);
        if(!Files.exists(this.storagePath))
            Files.createDirectories(this.storagePath);
        jsonMapper = new ObjectMapper();
        cache.addAll(getAll(this.storagePath));
        this.entityName = entityName;
    }


    public T create(T entity) {
        if(getEntityByName(entity.getName()).isPresent()){
            throw new EntityAlreadyExistsException(entity.getName());
        }
        String newId = UUID.randomUUID().toString();
        try {
            Path createdFile = Files.createFile(getEntityFilePath(newId));
            T newEntity = entity.copy();
            newEntity.setId(newId);
            entity.setId(newId);
            jsonMapper.writeValue(createdFile.toFile(),newEntity);
            cache.add(newEntity);
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error while creating file",e);
        }

        return entity;
    }

    public Optional<T> read(String id) {
        return cache.stream().filter(entity -> entity.getId().equals(id)).findAny().map(Copyable::copy);
    }


    public void update(T entity) {
        Optional<T> entityByName = getEntityByName(entity.getName());
        if(entityByName.isPresent()){
            try {
                entity.setId(entityByName.get().getId());
                Path entityFilePath = getEntityFilePath(entityByName.get().getId());
                jsonMapper.writeValue(entityFilePath.toFile(),entity);
                cache.remove(entityByName.get());
                cache.add(entity.copy());
            } catch (IOException e) {
                throw new StoragePathInvalidException(e);
            }
        } else {
            throw new EntityNotFoundException(entity.getName());
        }

    }


    public void delete(String id) {
        Optional<T> entityById = getEntityById(id);
        if(entityById.isEmpty())
            throw new EntityNotFoundException(id);
        Path entityFilePath = getEntityFilePath(id);
        try {
            Files.delete(entityFilePath);
            cache.remove(entityById.get());
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error in deleting file " + entityFilePath.getFileName());
        }


    }

    private Optional<T> getEntityByName(String name){
        return cache.stream().filter(infoSchema -> infoSchema.getName().equals(name)).findFirst();
    }

    private Optional<T> getEntityById(String id){
        return cache.stream().filter(infoSchema -> infoSchema.getId().equals(id)).findFirst();
    }

    private List<T> getAll(Path storagePath) {
        try(Stream<Path> files = Files.walk(storagePath)){
            return files.filter(file -> file.endsWith("." + entityName))
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

    private Path getEntityFilePath(String newId) {
        return storagePath.resolve(newId + "." + entityName);
    }
}
