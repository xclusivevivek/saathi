package com.vvsoft.saathi;

import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundException> handle(EntityNotFoundException exception) {
        logError(exception);
        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<EntityAlreadyExistsException> handle(EntityAlreadyExistsException exception) {
        logError(exception);
        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }

    private static void logError(Exception exception) {
        log.error("Responding with error {}:{}", exception.getClass().getName(), exception.getMessage());
    }
}
