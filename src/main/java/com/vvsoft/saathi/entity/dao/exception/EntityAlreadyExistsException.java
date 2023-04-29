package com.vvsoft.saathi.entity.dao.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String entityName){
        super(String.format("Entity %s already exists",entityName));
    }
}
