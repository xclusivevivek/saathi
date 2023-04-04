package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.schema.SchemaRepository;
import com.vvsoft.saathi.test.util.StorageUtil;
import com.vvsoft.saathi.test.util.TestSchemaProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfoRecordRepositoryGenericImplTest {

    @Autowired
    private InfoRecordRepository infoRecordRepository;

    @Autowired
    private SchemaRepository schemaRepository;

    @Value("${app.record.storage.path}")
    private String recordPath;

    @Autowired
    private TestSchemaProvider schemaProvider;

    @AfterEach
    void cleanup() throws IOException {
        StorageUtil.clearDirectory(recordPath);
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
        assertThrows(EntityAlreadyExistsException.class,() -> {
            infoRecordRepository.create(duplicateRecord);});
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
        InfoRecord result = infoRecordRepository.create(infoRecord);
        infoRecordRepository.delete(recordName);
        Optional<InfoRecord> record = infoRecordRepository.find(recordName);
        assertTrue(record.isEmpty());
    }

    @Test
    void cannotDeleteNonExistingInfoSchema(){
        String recordName = "EntityForUpdateWithoutCreate";
        assertThrows(EntityNotFoundException.class,() -> infoRecordRepository.delete(recordName));
    }

}