package com.vvsoft.saathi.info.record.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InfoRecordDto {
    String name;
    String schemaName;
    Map<String,String> values;
}
