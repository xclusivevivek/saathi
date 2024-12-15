package com.vvsoft.saathi.info.record.crud;

import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.record.presentation.InfoRecordDto;
import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.record.model.SimpleRecordValue;
import com.vvsoft.saathi.info.schema.crud.SchemaRepository;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class InfoRecordServiceImpl implements InfoRecordCrudService {

    private final InfoRecordRepository infoRecordRepository;
    private final SchemaRepository schemaRepository;

    public InfoRecordServiceImpl(InfoRecordRepository infoRecordRepository, SchemaRepository schemaRepository) {
        this.infoRecordRepository = infoRecordRepository;
        this.schemaRepository = schemaRepository;
    }

    @Override
    public InfoRecord create(InfoRecordDto dto) {
        Optional<InfoSchema> infoSchema = schemaRepository.find(dto.getSchemaName());
        if(infoSchema.isEmpty())
            throw new EntityNotFoundException("Schema: " + dto.getSchemaName());

        SimpleRecordValue recordValue = SimpleRecordValue.builder(infoSchema.get()).addValues(dto.getValues()).build();
        InfoRecord infoRecord = new InfoRecord(dto.getName(), recordValue);
        return infoRecordRepository.create(infoRecord);
    }

    @Override
    public List<InfoRecord> getAll() {
        return infoRecordRepository.findAll();
    }

    @Override
    public Optional<InfoRecord> read(String name) {
        return infoRecordRepository.find(name);
    }

    @Override
    public InfoRecord update(InfoRecordDto infoRecordDto) {
        Optional<InfoRecord> maybeInfoRecord = infoRecordRepository.find(infoRecordDto.getName());
        if(maybeInfoRecord.isEmpty())
            throw new EntityNotFoundException("InfoRecord : " + infoRecordDto.getName());
        InfoRecord infoRecord = maybeInfoRecord.get();
        infoRecord.updateValues(infoRecordDto.getValues());
        infoRecordRepository.update(infoRecord);
        log.info("Record {} Updated",infoRecord.getName());
        return infoRecord;
    }

    @Override
    public void delete(String recordName) {
        infoRecordRepository.delete(recordName);
    }
}
