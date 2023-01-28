package com.vvsoft.saathi.info.schema.dao;

import com.vvsoft.saathi.info.schema.dao.exception.SchemaAlreadyExistsException;
import com.vvsoft.saathi.info.schema.model.InfoSchema;

import java.util.Optional;

public interface InfoSchemaDao<T extends InfoSchema> {
    T create(T schema) throws SchemaAlreadyExistsException;
    Optional<T> read(String id);
    void update(T schema);
    void delete(String id);
}
