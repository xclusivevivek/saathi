package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.info.persistance.GenericLocalStorageDao;
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
    public GenericDao<InfoSchema> getInfoSchemaDao() throws IOException {
        return new GenericLocalStorageDao<>(schemaStoragePath,"schema",loadOnStartup);
    }

    @Bean
    public SchemaRepository getInfoSchemaRepo(GenericDao<InfoSchema> dao) throws IOException {
        return new SchemaRepositoryImpl(dao);
    }
}
