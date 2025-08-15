package com.invest.models;

import lombok.Builder;

@Builder
public record ValidationErrorDto(String field, String message) {}
