package com.invex.employee.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {

    private Long id;
    private String firstName;
    private String middleName;
    private String paternalLastName;
    private String maternalLastName;
    private Integer age;
    private Gender gender;
    private LocalDate birthDate;
    private String position;
    private LocalDateTime createdAt;
    private boolean active;

    public Employee() {
    }

    public Employee(
            Long id,
            String firstName,
            String middleName,
            String paternalLastName,
            String maternalLastName,
            Integer age,
            Gender gender,
            LocalDate birthDate,
            String position,
            LocalDateTime createdAt,
            Boolean active) {

        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.paternalLastName = paternalLastName;
        this.maternalLastName = maternalLastName;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
        this.position = position;
        this.createdAt = createdAt;
        this.active = active;
    }

    // getters and setters
}