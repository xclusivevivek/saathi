package com.vvsoft.saathi.error.rest;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class ApiErrorUtil {

    private ApiErrorUtil(){}

    public static ApiError buildApiError(Exception exception) {
        return ApiError.builder().errorMessage(exception.getMessage())
                .debugDetail(buildDebugDetail(exception)).build();
    }

    public static String buildDebugDetail(Exception exception) {
        return String.format("Caused By : %s | Occurred At : %s", exception.getClass().getName(),
                Arrays.stream(exception.getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(""));
    }

    public static void logError(Exception exception) {
        log.error("Handling Exception {}:{}", exception.getClass().getSimpleName(), exception.getMessage());
    }
}
