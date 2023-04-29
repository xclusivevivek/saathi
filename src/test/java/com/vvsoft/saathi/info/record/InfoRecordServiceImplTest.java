package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.record.service.InfoRecordCrudService;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.test.util.StorageUtil;
import com.vvsoft.saathi.test.util.TestSchemaProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfoRecordServiceImplTest {

    @Autowired
    private TestSchemaProvider schemaProvider;

    @Autowired
    private InfoRecordCrudService infoRecordService;

    @Autowired
    private StorageUtil storageUtil;

    @BeforeAll
    void init() throws IOException {
        storageUtil.clearRecordStoragePath();
    }

    @Test
    void canCreateInfoRecordFromExistingSchema() {
        InfoRecord infoRecord = infoRecordService.create(createTestRecordDto("TestInfoRecord", "100"));
        Optional<Object> actualAge = infoRecord.getRecordValue().getValue("Age");
        assertTrue(actualAge.isPresent());
        assertEquals(100L, actualAge.get());
    }

    @Test
    void whenSchemaIsInvalid_createInfoRecordThrowsException() {
        InfoSchema infoSchema = new InfoSchema("InvalidSchema", List.of());
        InfoRecordDto dto = InfoRecordDto.builder().schemaName(infoSchema.getName())
                .name("TestInfoRecord")
                .values(Map.of("Age", "100")).build();
        assertThrows(EntityNotFoundException.class,() -> infoRecordService.create(dto));
    }

    @Test
    void canGetAllRecords(){
        String recordName = "TestInfoRecordAll";
        InfoRecord infoRecord = infoRecordService.create(createTestRecordDto(recordName, "100"));
        Optional<Object> actualAge = infoRecord.getRecordValue().getValue("Age");
        assertTrue(actualAge.isPresent());
        assertEquals(100L, actualAge.get());
        List<InfoRecord> records = infoRecordService.getAll();
        assertTrue(records.stream().anyMatch( rec -> rec.getName().equals(recordName)));
    }

    @Test
    void whenRecordIsPresent_readReturnsTheRecord() {
        InfoRecord infoRecord = infoRecordService.create(createTestRecordDto("TestInfoRecord2", "100"));
        Optional<InfoRecord> readRecord = infoRecordService.read(infoRecord.getName());
        assertTrue(readRecord.isPresent());
        assertEquals(infoRecord.getId(),readRecord.get().getId());
    }

    @Test
    void whenRecordIsAbsent_readReturnsEmpty() {
        Optional<InfoRecord> readRecord = infoRecordService.read("Dummy");
        assertTrue(readRecord.isEmpty());
    }

    @Test
    void canUpdateExistingRecord() throws NoSuchFieldException {
        String recordName = "RecordForUpdate";
        InfoRecord infoRecord = infoRecordService.create(createTestRecordDto(recordName, "20"));
        InfoRecordDto updatedDto = InfoRecordDto.builder().name(infoRecord.getName())
                .schemaName(infoRecord.getRecordValue().getInfoSchema().getName())
                .values(Map.of("Age", "30"))
                .build();
        infoRecordService.update(updatedDto);
        Optional<InfoRecord> read = infoRecordService.read(recordName);
        assertTrue(read.isPresent());
        Optional<Object> readAge = read.get().getRecordValue().getValue("Age");
        assertTrue(readAge.isPresent());
        assertEquals(30L, readAge.get());
    }

    @Test
    void whenRecordIsAbsent_ThrowExceptionOnUpdate() {
        InfoRecordDto dto = InfoRecordDto.builder().name("Dummy").schemaName("DummySchema").build();
        assertThrows(EntityNotFoundException.class,() -> infoRecordService.update(dto));
    }

    @Test
    void canDeleteExistingRecord() {
        String recordName = "RecordForDelete";
        InfoRecord infoRecord = infoRecordService.create(createTestRecordDto(recordName, "20"));
        infoRecordService.delete(infoRecord.getName());
        Optional<InfoRecord> read = infoRecordService.read(recordName);
        assertTrue(read.isEmpty());
    }

    @Test
    void whenRecordIsAbsent_ThrowExceptionOnDelete() {
        InfoSchema infoSchema = new InfoSchema("InvalidSchema", List.of());
        InfoRecord infoRecord = new InfoRecord("Dummy", SimpleRecordValue.builder(infoSchema).build());
        String name = infoRecord.getName();
        assertThrows(EntityNotFoundException.class,() -> infoRecordService.delete(name));
    }

    private InfoRecordDto createTestRecordDto(String recordName, String ageValue) {
        InfoSchema infoSchema = schemaProvider.get();
        InfoRecordDto dto = InfoRecordDto.builder().schemaName(infoSchema.getName())
                .name(recordName)
                .values(Map.of("Age", ageValue)).build();
        return dto;
    }
}