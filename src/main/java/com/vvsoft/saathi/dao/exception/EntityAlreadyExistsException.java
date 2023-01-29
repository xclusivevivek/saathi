package com.vvsoft.saathi.dao.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String entityName){
        super(String.format("Entity %s already exists",entityName));
    }
}
