package com.vvsoft.saathi.info.record.presentation;

import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.schema.presentation.InfoSchemaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class InfoRecordDetailDto {
    private String id;
    private String name;
    private InfoSchemaDto schema;
    private Map<String,Object> values;

    public static InfoRecordDetailDto from(InfoRecord infoRecord){
        return InfoRecordDetailDto.builder()
                .id(infoRecord.getId())
                .schema(InfoSchemaDto.fromSchema(infoRecord.getRecordValue().getInfoSchema()))
                .values(infoRecord.getRecordValue().getValues())
                .name(infoRecord.getName())
                .build();
    }
}
