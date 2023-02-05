package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class SchemaServiceImpl implements SchemaRepository {
    private final GenericDao<InfoSchema> dao;
    @Autowired
    public SchemaServiceImpl(GenericDao<InfoSchema> dao) {
        this.dao = dao;
    }

    @Override
    public List<InfoSchema> findAll() {
        return dao.getAll();
    }

    @Override
    public InfoSchema create(InfoSchema schema) {
        log.info("Creating Schema: {}",schema);
        return dao.create(schema);
    }

    @Override
    public InfoSchema update(InfoSchema schema) {
        log.info("Updating Schema: {}",schema);
        dao.update(schema);
        return schema;
    }

    @Override
    public void delete(String name) {
        log.info("Deleting Schema with name: {}",name);
        dao.delete(name);
    }

    @Override
    public Optional<InfoSchema> find(String name) {
        return dao.read(name);
    }


}
