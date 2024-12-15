package com.vvsoft.saathi.info.schema.crud.api.controller;

import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.error.rest.ApiError;
import com.vvsoft.saathi.error.rest.ApiErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@Slf4j
public class EntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handle(EntityNotFoundException exception) {
        ApiErrorUtil.logError(exception);
        return ApiErrorUtil.buildApiError(exception);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handle(EntityAlreadyExistsException exception) {
        ApiErrorUtil.logError(exception);
        return ApiErrorUtil.buildApiError(exception);
    }

}
