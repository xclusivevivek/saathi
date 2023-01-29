package com.vvsoft.saathi.info.schema.dao;

import com.vvsoft.saathi.dao.GenericDao;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class InfoSchemaDaoImpl<T extends InfoSchema> implements InfoSchemaDao<T>{
    private final GenericDao<T> genericDao;

    @Autowired
    public InfoSchemaDaoImpl(GenericDao<T> genericDao) {
        this.genericDao = genericDao;
    }
    @Override
    public T create(T infoSchema) {
        return genericDao.create(infoSchema);
    }

    @Override
    public Optional<T> read(String id) {
        return genericDao.read(id);
    }

    @Override
    public void update(T entity) {
        genericDao.update(entity);
    }

    @Override
    public void delete(String id) {
        genericDao.delete(id);
    }
}
