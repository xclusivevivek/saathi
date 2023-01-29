package com.vvsoft.saathi.dao.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String schema) {
        super(String.format("Schema %s not found",schema));
    }

}
