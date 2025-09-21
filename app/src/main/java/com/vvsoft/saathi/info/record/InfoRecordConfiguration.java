package com.vvsoft.saathi.info.record;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.saathi.drobox.filesystem.DropboxFileSystem;
import com.vvsoft.saathi.entity.dao.GenericDao;
import com.vvsoft.saathi.entity.persistance.EntityPersistor;
import com.vvsoft.saathi.entity.persistance.GenericPersistenceDao;
import com.vvsoft.saathi.entity.persistance.LocalDiskStorageFileSystem;
import com.vvsoft.saathi.entity.persistance.StorageFileSystem;
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
    @Value("${app.filesystem}")
    private String fileSystemName;
    @Value("${app.dropbox.access_token}")
    private String dropboxAccessToken;

    @Bean
    public StorageFileSystem storageFileSystem(){
        if(fileSystemName.equals("DROPBOX")) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("saathi").build();
            DbxClientV2 dbxClientV2 = new DbxClientV2(requestConfig, dropboxAccessToken);
            return new DropboxFileSystem(dbxClientV2);
        }
        return new LocalDiskStorageFileSystem();
    }

    @Bean
    public EntityPersistor<InfoRecord> recordEntityPersistor(StorageFileSystem fileSystem){
        return new FileBasedEntityPersistor<InfoRecord>(schemaStoragePath,new ObjectMapper(),"record",fileSystem);
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
