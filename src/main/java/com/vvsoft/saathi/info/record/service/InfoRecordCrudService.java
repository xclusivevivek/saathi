package com.vvsoft.saathi.info.record.service;

import com.vvsoft.saathi.info.record.InfoRecord;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;

import java.util.List;

public interface InfoRecordCrudService {
    InfoRecord create(InfoRecordDto dto);
    List<InfoRecord> getAll();
}
