package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchemaServiceImpl implements SchemaService {
    private final GenericDao<InfoSchema> dao;
    @Autowired
    public SchemaServiceImpl(GenericDao<InfoSchema> dao) {
        this.dao = dao;
    }

    @Override
    public List<InfoSchema> getAll() {
        return dao.getAll();
    }
}
