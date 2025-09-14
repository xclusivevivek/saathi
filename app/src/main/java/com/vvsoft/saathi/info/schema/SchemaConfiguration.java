package com.vvsoft.saathi.info.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.entity.persistance.EntityPersistor;
import com.vvsoft.saathi.entity.persistance.GenericPersistenceDao;
import com.vvsoft.saathi.info.persistance.LocalStorageEntityPersistor;
import com.vvsoft.saathi.info.schema.crud.SchemaRepository;
import com.vvsoft.saathi.info.schema.crud.SchemaRepositoryImpl;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SchemaConfiguration {

    @Value("${app.schema.storage.path}")
    private String schemaStoragePath;

    @Value("${app.schema.loadOnStartup}")
    private boolean loadOnStartup;

    @Bean
    public GenericDao<InfoSchema> getInfoSchemaDao(EntityPersistor<InfoSchema> entityPersistor) throws IOException {
        return new GenericPersistenceDao<>(entityPersistor,loadOnStartup);
    }

    @Bean
    public SchemaRepository getInfoSchemaRepo(GenericDao<InfoSchema> dao) throws IOException {
        return new SchemaRepositoryImpl(dao);
    }

    @Bean
    public EntityPersistor<InfoSchema> getEntityPersistor(){
        return new LocalStorageEntityPersistor<>(schemaStoragePath,new ObjectMapper(),"schema");
    }
}
