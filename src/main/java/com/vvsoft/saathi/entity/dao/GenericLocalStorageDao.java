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

    public GenericLocalStorageDao(String storagePath, String entityName,boolean loadOnStartup) throws IOException {
        this.storagePath = Path.of(storagePath);
        this.entityName = entityName;
        jsonMapper = new ObjectMapper();
        if(!Files.exists(this.storagePath)) {
            Files.createDirectories(this.storagePath);
        }
        if(loadOnStartup)
            cache.addAll(getAll(this.storagePath));
    }

    public GenericLocalStorageDao(String storagePath, String entityName) throws IOException {
        this(storagePath,entityName,true);
    }

    @Override
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
    @Override
    public Optional<T> read(String name) {
        return cache.stream().filter(entity -> entity.getName().equals(name)).findAny().map(Copyable::copy);
    }

    @Override
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

    @Override
    public void delete(String entityName) {
        Optional<T> foundEntity = getEntityByName(entityName);
        if(foundEntity.isEmpty())
            throw new EntityNotFoundException(entityName);
        Path entityFilePath = getEntityFilePath(foundEntity.get().getId());
        try {
            Files.delete(entityFilePath);
            cache.remove(foundEntity.get());
        } catch (IOException e) {
            throw new StoragePathInvalidException("Error in deleting file " + entityFilePath.getFileName());
        }


    }

    @Override
    public List<T> getAll() {
        return cache.stream().map(Copyable::copy).collect(Collectors.toList());
    }

    private Optional<T> getEntityByName(String name){
        return cache.stream().filter(infoSchema -> infoSchema.getName().equals(name)).findFirst();
    }

    private List<T> getAll(Path storagePath) {
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

    private Path getEntityFilePath(String newId) {
        return storagePath.resolve(newId + "." + entityName);
    }
}
