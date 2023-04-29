package com.vvsoft.saathi.info.record.service;

import com.vvsoft.saathi.info.record.InfoRecord;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;

import java.util.List;
import java.util.Optional;

public interface InfoRecordCrudService {
    InfoRecord create(InfoRecordDto dto);
    List<InfoRecord> getAll();
    Optional<InfoRecord> read(String name);
    InfoRecord update(InfoRecordDto infoRecord);
    void delete(String recordName);
}
