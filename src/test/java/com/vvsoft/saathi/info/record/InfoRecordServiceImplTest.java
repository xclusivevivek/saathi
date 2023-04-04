package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.record.service.InfoRecordCrudService;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.test.util.TestSchemaProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class InfoRecordServiceImplTest {

    @Autowired
    private TestSchemaProvider schemaProvider;

    @Autowired
    private InfoRecordCrudService infoRecordService;
    @Test
    void canCreateInfoRecordFromExistingSchema() {
        InfoSchema infoSchema = schemaProvider.get();
        InfoRecordDto dto = InfoRecordDto.builder().schemaName(infoSchema.getName())
                .name("TestInfoRecord")
                .values(Map.of("Age", "100")).build();
        InfoRecord infoRecord = infoRecordService.create(dto);
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
        InfoSchema infoSchema = schemaProvider.get();
        InfoRecordDto dto = InfoRecordDto.builder().schemaName(infoSchema.getName())
                .name("TestInfoRecord2")
                .values(Map.of("Age", "100")).build();
        InfoRecord infoRecord = infoRecordService.create(dto);
        Optional<Object> actualAge = infoRecord.getRecordValue().getValue("Age");
        assertTrue(actualAge.isPresent());
        assertEquals(100L, actualAge.get());
        List<InfoRecord> records = infoRecordService.getAll();
        assertTrue(records.stream().anyMatch( rec -> rec.getName().equals("TestInfoRecord2")));
    }
}