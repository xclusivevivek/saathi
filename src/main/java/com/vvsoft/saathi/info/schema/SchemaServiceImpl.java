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
        InfoSchema infoSchema = dao.create(schema);
        log.info("Created Schema: {}",infoSchema);
        return infoSchema;
    }

    @Override
    public InfoSchema update(InfoSchema schema) {
        dao.update(schema);
        log.info("Updated Schema: {}",schema);
        return schema;
    }

    @Override
    public void delete(String name) {
        dao.delete(name);
        log.info("Deleted Schema with name: {}",name);
    }

    @Override
    public Optional<InfoSchema> find(String name) {
        return dao.read(name);
    }
}
