package com.vvsoft.saathi.info.record.presentation;

import com.vvsoft.saathi.info.record.model.InfoRecord;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
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

    public static InfoRecordDto from(InfoRecord infoRecord){
        return InfoRecordDto.builder()
                .name(infoRecord.getName())
                .schemaName(infoRecord.getRecordValue().getInfoSchema().getName())
                .values(convertValueMap(infoRecord.getRecordValue().getValues()))
                .build();
    }

    public static Map<String,String> convertValueMap(Map<String,Object> valueMap) {
        Map<String,String> convertedMap = new HashMap<>();
        valueMap.forEach((key,value) -> {
            convertedMap.put(key,value.toString());
        });
        return convertedMap;
    }
}
