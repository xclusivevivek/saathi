package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.record.service.InfoRecordCrudService;
import com.vvsoft.saathi.info.schema.SchemaRepository;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InfoRecordServiceImpl implements InfoRecordCrudService {

    private final InfoRecordRepository infoRecordRepository;
    private final SchemaRepository schemaRepository;

    @Autowired
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
