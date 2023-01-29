package com.vvsoft.saathi.info.schema;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.entity.dao.GenericLocalStorageDao;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SchemaConfiguration {

    @Value("${app.schema.storage.path}")
    private String schemaStoragePath;
    @Bean
    public GenericDao<InfoSchema> getInfoSchemaDao() throws IOException {
        return new GenericLocalStorageDao<>(schemaStoragePath,"schema");
    }
}
