package com.vvsoft.saathi.info.schema.dao.exception;

public class StoragePathInvalidException extends RuntimeException{
    public StoragePathInvalidException(String message) {
        super(message);
    }

    public StoragePathInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
