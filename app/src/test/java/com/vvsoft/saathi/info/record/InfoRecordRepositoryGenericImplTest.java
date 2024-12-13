package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.GenericLocalStorageDao;
import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.SchemaRepository;
import com.vvsoft.saathi.test.util.StorageUtil;
import com.vvsoft.saathi.test.util.TestSchemaProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfoRecordRepositoryGenericImplTest {

    @Autowired
    private InfoRecordRepository infoRecordRepository;

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private TestSchemaProvider schemaProvider;

    @Autowired
    private StorageUtil storageUtil;

    @AfterEach
    void cleanup() throws IOException {
        storageUtil.clearRecordStoragePath();
    }

    @Test
    void canCreateRecord(){
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","bar").addValue("Age","20").
                build();
        InfoRecord infoRecord = new InfoRecord("Foo",recordValue);
        InfoRecord resultRecord = infoRecordRepository.create(infoRecord);
        RecordValue resultRecordValue = resultRecord.getRecordValue();
        assertEquals("bar",resultRecordValue.getValue("Name").get());
    }

    @Test
    void cannotCreateRecordIfAlreadyExists(){
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","bar").addValue("Age","26").
                build();
        InfoRecord infoRecord = new InfoRecord("BarDetails",recordValue);
        InfoRecord firstRecord = infoRecordRepository.create(infoRecord);
        InfoRecord duplicateRecord = new InfoRecord("BarDetails",SimpleRecordValue.builder(schemaProvider.get()).build());
        assertThrows(EntityAlreadyExistsException.class,() -> infoRecordRepository.create(duplicateRecord));
    }

    @Test
    void canFindExistingInfoSchema(){
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","SavedEntity").addValue("Age","1").
                build();
        String recordName = "ExistingDetails";
        InfoRecord infoRecord = new InfoRecord(recordName,recordValue);
        infoRecordRepository.create(infoRecord);
        Optional<InfoRecord> record = infoRecordRepository.find(recordName);
        assertTrue(record.isPresent());
        assertEquals(recordName,record.get().getName());
    }

    @Test
    void canUpdateExistingInfoSchema() throws NoSuchFieldException {
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","SavedEntity").addValue("Age","1").
                build();
        String recordName = "EntityForUpdate";
        InfoRecord infoRecord = new InfoRecord(recordName,recordValue);
        InfoRecord result = infoRecordRepository.create(infoRecord);
        result.getRecordValue().update("Age","23");
        infoRecordRepository.update(result);
        Optional<InfoRecord> returnedRecord = infoRecordRepository.find(recordName);
        assertTrue(returnedRecord.isPresent());
        RecordValue recordValue1 = returnedRecord.get().getRecordValue();
        assertEquals("23", recordValue1.getValue("Age").get().toString());
    }

    @Test
    void cannotUpdateNonExistingInfoSchema() throws NoSuchFieldException {
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","SavedEntity").addValue("Age","1").
                build();
        String recordName = "EntityForUpdateWithoutCreate";
        InfoRecord infoRecord = new InfoRecord(recordName,recordValue);
        infoRecord.getRecordValue().update("Age","23");
        assertThrows(EntityNotFoundException.class,() -> {infoRecordRepository.update(infoRecord);});
    }

    @Test
    void canDeleteExistingInfoSchema(){
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","SavedEntity").addValue("Age","1").
                build();
        String recordName = "EntityForDelete";
        InfoRecord infoRecord = new InfoRecord(recordName,recordValue);
        infoRecordRepository.create(infoRecord);
        infoRecordRepository.delete(recordName);
        Optional<InfoRecord> record = infoRecordRepository.find(recordName);
        assertTrue(record.isEmpty());
    }

    @Test
    void cannotDeleteNonExistingInfoSchema(){
        String recordName = "EntityForUpdateWithoutCreate";
        assertThrows(EntityNotFoundException.class,() -> infoRecordRepository.delete(recordName));
    }

    @Test
    void canGetAllInfoRecords(){
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","bar").addValue("Age","20").
                build();
        InfoRecord infoRecord = new InfoRecord("Foo2",recordValue);
        InfoRecord resultRecord = infoRecordRepository.create(infoRecord);
        RecordValue resultRecordValue = resultRecord.getRecordValue();
        assertEquals("bar",resultRecordValue.getValue("Name").get());
        List<InfoRecord> records = infoRecordRepository.findAll();
        assertTrue(records.stream().anyMatch( rec -> rec.getName().equals("Foo2")));
    }

    @Test
    void canLoadExistingInfoRecordOnStartup() throws IOException {
        String recordName = "Foo3";
        SimpleRecordValue recordValue = SimpleRecordValue.builder(schemaProvider.get()).addValue("Name","bar").addValue("Age","20").
                build();
        InfoRecord infoRecord = new InfoRecord(recordName,recordValue);
        InfoRecord resultRecord = infoRecordRepository.create(infoRecord);
        RecordValue resultRecordValue = resultRecord.getRecordValue();
        assertEquals("bar",resultRecordValue.getValue("Name").get());
        GenericLocalStorageDao<InfoRecord> dao = new GenericLocalStorageDao<>(storageUtil.getRecordStoragePath(), "record", true);
        InfoRecordRepositoryGenericImpl repo = new InfoRecordRepositoryGenericImpl(dao);
        Optional<InfoRecord> record = repo.find(recordName);
        assertEquals(recordName,record.get().getName());
    }

}