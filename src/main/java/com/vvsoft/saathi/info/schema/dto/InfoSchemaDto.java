package com.vvsoft.saathi.info.schema.dto;

import com.vvsoft.saathi.info.schema.model.InfoSchema;
import com.vvsoft.saathi.info.schema.model.field.SimpleField;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;


@Data
@Builder
public class InfoSchemaDto{
    private String name;

    @Singular
    private List<SimpleField> fields;

    public InfoSchema toSchema(){
        return new InfoSchema(name,fields);
    }
}
