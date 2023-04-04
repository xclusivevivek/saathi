package com.vvsoft.saathi.info.record;

import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;
import com.vvsoft.saathi.info.record.service.InfoRecordCrudService;
import com.vvsoft.saathi.info.schema.SchemaRepository;
import com.vvsoft.saathi.info.schema.model.InfoSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
}
