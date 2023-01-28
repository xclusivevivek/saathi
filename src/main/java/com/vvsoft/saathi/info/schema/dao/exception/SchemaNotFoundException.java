package com.vvsoft.saathi.info.schema.dao.exception;

public class SchemaNotFoundException extends RuntimeException {
    public SchemaNotFoundException(String schema) {
        super(String.format("Schema %s not found",schema));
    }

}
