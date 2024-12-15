package com.vvsoft.saathi.info.record.crud;

import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.record.presentation.InfoRecordDto;

import java.util.List;
import java.util.Optional;

public interface InfoRecordCrudService {
    InfoRecord create(InfoRecordDto dto);
    List<InfoRecord> getAll();
    Optional<InfoRecord> read(String name);
    InfoRecord update(InfoRecordDto infoRecord);
    void delete(String recordName);
}
