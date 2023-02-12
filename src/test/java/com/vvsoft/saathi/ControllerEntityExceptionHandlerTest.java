package com.vvsoft.saathi;

import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.entity.dao.exception.InvalidEntityFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerEntityExceptionHandlerTest {

    private ControllerEntityExceptionHandler exceptionHandler;

    @BeforeEach
    public void setupTest() {
        exceptionHandler = new ControllerEntityExceptionHandler();
    }
    @Test
    void testProperResponseToEntityNotFoundException(){
        EntityNotFoundException exception = new EntityNotFoundException("foo");
        ResponseEntity<EntityNotFoundException> errorResponse = exceptionHandler.handle(exception);
        assertEquals(HttpStatus.NOT_FOUND,errorResponse.getStatusCode());
        assertEquals("Entity foo not found",errorResponse.getBody().getMessage());
    }

    @Test
    void testProperResponseToEntityAlreadyExistsException(){
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException("foo");
        ResponseEntity<EntityAlreadyExistsException> response = exceptionHandler.handle(exception);
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals("Entity foo already exists",response.getBody().getMessage());
    }

}