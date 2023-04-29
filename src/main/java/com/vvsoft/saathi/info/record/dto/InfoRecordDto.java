package com.vvsoft.saathi.info.record.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
@ToString
public class InfoRecordDto {
    private String name;
    private String schemaName;
    private Map<String,String> values;

    public void setValue(String field,String value){
        values.put(field,value);
    }
}
