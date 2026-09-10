package com.invex.employee.infrastructure.adapter.in.rest.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
@Schema(description = "Standard error response returned by the API")
public class ErrorResponse {

    @Schema(
            description = "Date and time when the error occurred",
            example = "2026-09-09T12:30:45"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "HTTP status code",
            example = "400"
    )
    private int status;

    @Schema(
            description = "HTTP status description",
            example = "Bad Request"
    )
    private String error;

    @Schema(
            description = "Application-specific error code",
            example = "VALIDATION_ERROR"
    )
    private String code;

    @Schema(
            description = "Error description",
            example = "Request validation failed"
    )
    private String message;

    @Schema(
            description = "Request path where the error occurred",
            example = "/employees"
    )
    private String path;

    @Schema(
            description = "Validation errors associated with request fields",
            example = "{\"firstName\":\"First name is required\",\"age\":\"Age must be greater than zero\"}",
            nullable = true
    )
    private Map<String, String> details;
}