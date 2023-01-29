package com.vvsoft.saathi.dao.exception;

public class StoragePathInvalidException extends RuntimeException{
    public StoragePathInvalidException(String message) {
        super(message);
    }

    public StoragePathInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoragePathInvalidException(Throwable cause) {
        super(cause);
    }
}
