package com.invex.employee.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.invex.employee.domain.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Employee information returned by the API")
@Getter
@AllArgsConstructor
public class EmployeeResponse {

    @Schema(
            description = "Unique employee identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Employee first name",
            example = "John"
    )
    private String firstName;

    @Schema(
            description = "Employee middle name",
            example = "Michael"
    )
    private String middleName;

    @Schema(
            description = "Employee paternal last name",
            example = "Smith"
    )
    private String paternalLastName;

    @Schema(
            description = "Employee maternal last name",
            example = "Johnson"
    )
    private String maternalLastName;

    @Schema(
            description = "Employee age",
            example = "35"
    )
    private Integer age;

    @Schema(
            description = "Employee gender",
            example = "MALE",
            allowableValues = {"MALE", "FEMALE", "OTHER"}
    )
    private Gender gender;

    @Schema(
            description = "Employee birth date in dd-MM-yyyy format",
            example = "15-08-1990"
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(
            description = "Employee job position",
            example = "Senior Software Engineer"
    )
    private String position;

    @Schema(
            description = "Date and time when the employee was registered in the system",
            example = "2026-09-09T12:30:45"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Indicates whether the employee is active",
            example = "true"
    )
    private Boolean active;
}