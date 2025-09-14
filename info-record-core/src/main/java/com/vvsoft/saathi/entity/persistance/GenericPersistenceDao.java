package com.vvsoft.saathi.entity.persistance;


import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.model.Copyable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GenericPersistenceDao<T extends NamedEntity & Copyable<T>> implements GenericDao<T> {
    private final List<T> cache = new ArrayList<>();
    private final EntityPersistor<T> entityPersistor;

    public GenericPersistenceDao(EntityPersistor<T> entityPersistor, boolean loadOnStartup) throws IOException {
        this.entityPersistor = entityPersistor;
        entityPersistor.initialize();
        if(loadOnStartup)
            cache.addAll(entityPersistor.getAll());
    }

    public GenericPersistenceDao(EntityPersistor<T> entityPersistor) throws IOException {
        this( entityPersistor,true);
    }

    @Override
    public T create(T entity) {
        if(searchEntityByName(entity.getName()).isPresent()){
            throw new EntityAlreadyExistsException(entity.getName());
        }
        String newId = UUID.randomUUID().toString();
        T newEntity = entity.copy();
        newEntity.setId(newId);
        entity.setId(newId);
        entityPersistor.persistEntity(newEntity);
        cache.add(newEntity);
        return entity;
    }

    @Override
    public Optional<T> read(String name) {
        return cache.stream().filter(entity -> entity.getName().equals(name)).findAny().map(Copyable::copy);
    }

    @Override
    public void update(T entity) {
        Optional<T> entityByName = searchEntityByName(entity.getName());
        if(entityByName.isPresent()){
            entity.setId(entityByName.get().getId());
            entityPersistor.updateEntity(entity);
            cache.remove(entityByName.get());
            cache.add(entity.copy());
        } else {
            throw new EntityNotFoundException(entity.getName());
        }

    }

    @Override
    public void delete(String entityName) {
        Optional<T> foundEntity = searchEntityByName(entityName);
        if(foundEntity.isEmpty())
            throw new EntityNotFoundException(entityName);
        entityPersistor.deleteEntity(foundEntity.get());
        cache.remove(foundEntity.get());
    }

    @Override
    public List<T> getAll() {
        return cache.stream().map(Copyable::copy).collect(Collectors.toList());
    }

    private Optional<T> searchEntityByName(String name){
        return cache.stream().filter(infoSchema -> infoSchema.getName().equals(name)).findFirst();
    }

}