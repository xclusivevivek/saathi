package com.vvsoft.saathi.info.schema.crud.api.controller;

import com.vvsoft.saathi.error.rest.ApiError;
import com.vvsoft.saathi.error.rest.ApiErrorUtil;
import com.vvsoft.saathi.info.record.model.exception.FieldNotFoundInSchemaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class SchemaErrorHandler {

    @ExceptionHandler(FieldNotFoundInSchemaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleInvalidSchemaField(FieldNotFoundInSchemaException exception){
        ApiErrorUtil.logError(exception);
        return ApiErrorUtil.buildApiError(exception);
    }
}
