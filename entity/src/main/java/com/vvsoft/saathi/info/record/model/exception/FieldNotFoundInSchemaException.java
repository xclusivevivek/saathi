package com.vvsoft.saathi.info.record.model.exception;

import lombok.Getter;

@Getter
public class FieldNotFoundInSchemaException extends RuntimeException{
    final String fieldName;
    final String schemaName;

    public FieldNotFoundInSchemaException(String fieldName, String schemaName) {
        super(String.format("Field %s not found in schema %s",fieldName,schemaName));
        this.fieldName = fieldName;
        this.schemaName = schemaName;
    }
}
