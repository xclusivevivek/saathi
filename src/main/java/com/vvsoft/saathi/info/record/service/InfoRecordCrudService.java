package com.vvsoft.saathi.info.record.service;

import com.vvsoft.saathi.info.record.InfoRecord;
import com.vvsoft.saathi.info.record.dto.InfoRecordDto;

public interface InfoRecordCrudService {
    InfoRecord create(InfoRecordDto dto);
}
