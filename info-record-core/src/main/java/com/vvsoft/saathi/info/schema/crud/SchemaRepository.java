package com.vvsoft.saathi.info.schema.crud;

import com.vvsoft.saathi.info.schema.model.InfoSchema;

import java.util.List;
import java.util.Optional;


public interface SchemaRepository {
    List<InfoSchema> findAll();
    InfoSchema create(InfoSchema schema);
    InfoSchema update(InfoSchema schema);
    void delete(String name);
    Optional<InfoSchema> find(String name);
}
