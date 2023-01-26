package com.vvsoft.saathi.info.schema.dao;

import com.vvsoft.saathi.info.schema.model.InfoSchema;

public interface InfoSchemaDao<T extends InfoSchema> {
    void create(T schema);
    T read(long id);
    void update(T schema);
    void delete(long id);
}
