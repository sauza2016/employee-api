package com.invex.employee.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.invex.employee.domain.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Request used to update an employee. All fields are optional.")
@Getter
@Setter
public class EmployeeUpdateRequest {

    @Schema(
            description = "Employee first name",
            example = "John"
    )
    @Size(
            max = 100,
            message = "First name must not exceed 100 characters"
    )
    private String firstName;

    @Schema(
            description = "Employee middle name",
            example = "Michael"
    )
    @Size(
            max = 100,
            message = "Middle name must not exceed 100 characters"
    )
    private String middleName;

    @Schema(
            description = "Employee paternal last name",
            example = "Smith"
    )
    @Size(
            max = 100,
            message = "Paternal last name must not exceed 100 characters"
    )
    private String paternalLastName;

    @Schema(
            description = "Employee maternal last name",
            example = "Johnson"
    )
    @Size(
            max = 100,
            message = "Maternal last name must not exceed 100 characters"
    )
    private String maternalLastName;

    @Schema(
            description = "Employee age",
            example = "35"
    )
    @Positive(
            message = "Age must be greater than zero"
    )
    private Integer age;

    @Schema(
            description = "Employee gender",
            example = "MALE",
            allowableValues = {
                    "MALE",
                    "FEMALE",
                    "OTHER"
            }
    )
    private Gender gender;

    @Schema(
            description = "Employee birth date in dd-MM-yyyy format",
            example = "15-08-1990"
    )
    @Past(
            message = "Birth date must be in the past"
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(
            description = "Employee job position",
            example = "Senior Software Engineer"
    )
    @Size(
            max = 150,
            message = "Position must not exceed 150 characters"
    )
    private String position;

    @Schema(
            description = "Indicates whether the employee is active",
            example = "true"
    )
    private Boolean active;
}