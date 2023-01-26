package com.vvsoft.saathi.info.schema.field;

import lombok.Getter;

@Getter
public class SimpleField implements Field{
    private final String key;
    private final String displayName;
    private final FieldType fieldType;

    public SimpleField(String key, String displayName, FieldType fieldType) {
        this.key = key;
        this.displayName = displayName;
        this.fieldType = fieldType;
    }

    public SimpleField(String key, FieldType fieldType) {
        this(key,key,fieldType);
    }
}
