package com.vvsoft.saathi.info.record.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
@ToString
public class InfoRecordDto {
    String name;
    String schemaName;
    Map<String,String> values;
}
