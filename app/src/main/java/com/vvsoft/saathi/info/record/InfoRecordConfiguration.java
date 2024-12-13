package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.info.GenericLocalStorageDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class InfoRecordConfiguration {
    @Value("${app.record.storage.path}")
    private String schemaStoragePath;
    @Value("${app.record.loadOnStartup}")
    private boolean loadOnStartup;

    @Bean
    public GenericDao<InfoRecord> getInfoRecordDao() throws IOException {
        return new GenericLocalStorageDao<>(schemaStoragePath,"record",loadOnStartup);
    }

}
