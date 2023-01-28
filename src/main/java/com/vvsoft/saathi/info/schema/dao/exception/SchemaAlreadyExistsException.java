package com.vvsoft.saathi.info.schema.dao.exception;

public class SchemaAlreadyExistsException extends Exception {
    private final String schemaName;

    public SchemaAlreadyExistsException(String schemaName){
        super(String.format("Schema %s already exists",schemaName));
        this.schemaName = schemaName;
    }
}
