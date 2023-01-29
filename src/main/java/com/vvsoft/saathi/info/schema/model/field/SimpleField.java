package com.vvsoft.saathi.info.schema.model.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SimpleField implements Field{
    @EqualsAndHashCode.Include
    private final String key;
    private final String displayName;
    private final FieldType fieldType;

    @JsonCreator
    public SimpleField(@JsonProperty("key") String key, @JsonProperty("displayName")String displayName, @JsonProperty("fieldType")FieldType fieldType) {
        this.key = key;
        this.displayName = displayName;
        this.fieldType = fieldType;
    }

    public SimpleField(String key, FieldType fieldType) {
        this(key,key,fieldType);
    }
}
