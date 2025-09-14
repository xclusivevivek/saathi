package com.vvsoft.saathi.entity.persistance;

import com.vvsoft.saathi.entity.NamedEntity;
import com.vvsoft.saathi.info.schema.model.Copyable;

import java.io.IOException;
import java.util.Collection;

public interface EntityPersistor<T extends NamedEntity & Copyable<T>> {
    void initialize() throws IOException;
    Collection<T> getAll();
    void persistEntity(T newEntity);
    void updateEntity(T entity);
    void deleteEntity(T entity);
}
