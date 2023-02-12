package com.vvsoft.saathi.error.rest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiError {
    String errorMessage;
    String debugDetail;
}
