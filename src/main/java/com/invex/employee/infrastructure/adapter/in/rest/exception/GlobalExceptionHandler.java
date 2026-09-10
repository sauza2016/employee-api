package com.invex.employee.infrastructure.adapter.in.rest.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.invex.employee.application.exception.EmployeeNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "EMPLOYEE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> details = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        details.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getRequestURI(),
                details
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> details = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                details.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getRequestURI(),
                details
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error processing request", ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "INVALID_REQUEST_BODY",
                "Request body is invalid or contains an invalid value",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }
}