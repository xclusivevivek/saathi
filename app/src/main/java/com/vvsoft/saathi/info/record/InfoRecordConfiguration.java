package com.vvsoft.saathi.info.record;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.entity.persistance.EntityPersistor;
import com.vvsoft.saathi.entity.persistance.GenericPersistenceDao;
import com.vvsoft.saathi.entity.persistance.LocalDiskStorageFileSystem;
import com.vvsoft.saathi.info.persistance.FileBasedEntityPersistor;
import com.vvsoft.saathi.info.record.crud.InfoRecordCrudService;
import com.vvsoft.saathi.info.record.crud.InfoRecordRepository;
import com.vvsoft.saathi.info.record.crud.InfoRecordRepositoryGenericImpl;
import com.vvsoft.saathi.info.record.crud.InfoRecordServiceImpl;
import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.schema.crud.SchemaRepository;
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
    public EntityPersistor<InfoRecord> recordEntityPersistor(){
        return new FileBasedEntityPersistor<InfoRecord>(schemaStoragePath,new ObjectMapper(),"record",new LocalDiskStorageFileSystem());
    }

    @Bean
    public GenericDao<InfoRecord> getInfoRecordDao(EntityPersistor<InfoRecord> recordEntityPersistor) throws IOException {
        return new GenericPersistenceDao<>(recordEntityPersistor,loadOnStartup);
    }

    @Bean
    public InfoRecordCrudService getInfoRecordCrudService(InfoRecordRepository infoRecordRepository, SchemaRepository schemaRepository) {
        return new InfoRecordServiceImpl(infoRecordRepository,schemaRepository);
    }

    @Bean
    public InfoRecordRepository getInfoRecordRepo(GenericDao<InfoRecord> dao) {
        return new InfoRecordRepositoryGenericImpl(dao);
    }


}
