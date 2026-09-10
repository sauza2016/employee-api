package com.invex.employee.infrastructure.adapter.in.rest.dto;

import java.time.LocalDate;

import com.invex.employee.domain.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeCommand {

    private String firstName;
    private String middleName;
    private String paternalLastName;
    private String maternalLastName;
    private Integer age;
    private Gender gender;
    private LocalDate birthDate;
    private String position;
    private Boolean active;
}