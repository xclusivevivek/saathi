package com.vvsoft.saathi.error.rest;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ApiError {
    String errorMessage;
    String debugDetail;
}
