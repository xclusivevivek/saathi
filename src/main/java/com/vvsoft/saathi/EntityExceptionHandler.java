package com.vvsoft.saathi;

import com.vvsoft.saathi.entity.dao.exception.EntityAlreadyExistsException;
import com.vvsoft.saathi.entity.dao.exception.EntityNotFoundException;
import com.vvsoft.saathi.error.rest.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
@Slf4j
public class EntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handle(EntityNotFoundException exception) {
        logError(exception);
        return buildApiError(exception);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handle(EntityAlreadyExistsException exception) {
        logError(exception);
        return buildApiError(exception);
    }

    private static ApiError buildApiError(Exception exception) {
        return ApiError.builder().errorMessage(exception.getMessage())
                .debugDetail(buildDebugDetail(exception)).build();
    }

    private static String buildDebugDetail(Exception exception) {
        return String.format("Caused By : %s | Occurred At : %s", exception.getClass().getName(),
                Arrays.stream(exception.getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(""));
    }

    private static void logError(Exception exception) {
        log.error("Handling Exception {}:{}", exception.getClass().getSimpleName(), exception.getMessage());
    }
}
