package com.invex.employee.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.invex.employee.domain.model.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Request used to create an employee")
@Getter
@Setter
public class EmployeeCreateRequest {

    @Schema(
            description = "Employee first name",
            example = "John"
    )
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Schema(
            description = "Employee middle name",
            example = "Michael"
    )
    @Size(max = 100, message = "Middle name must not exceed 100 characters")
    private String middleName;

    @Schema(
            description = "Employee paternal last name",
            example = "Smith"
    )
    @NotBlank(message = "Paternal last name is required")
    @Size(max = 100, message = "Paternal last name must not exceed 100 characters")
    private String paternalLastName;

    @Schema(
            description = "Employee maternal last name",
            example = "Johnson"
    )
    @Size(max = 100, message = "Maternal last name must not exceed 100 characters")
    private String maternalLastName;

    @Schema(
            description = "Employee age",
            example = "35"
    )
    @NotNull(message = "Age is required")
    @Positive(message = "Age must be greater than zero")
    private Integer age;

    @Schema(
            description = "Employee gender",
            example = "MALE",
            allowableValues = {"MALE", "FEMALE", "OTHER"}
    )
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Schema(
            description = "Employee birth date in dd-MM-yyyy format",
            example = "15-08-1990"
    )
    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(
            description = "Employee job position",
            example = "Software Engineer"
    )
    @NotBlank(message = "Position is required")
    @Size(max = 150, message = "Position must not exceed 150 characters")
    private String position;

    @Schema(
            description = "Indicates whether the employee is active",
            example = "true",
            defaultValue = "true"
    )
    private Boolean active;
}