package com.invex.employee.infrastructure.adapter.out.persistence;

import com.invex.employee.domain.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "paternal_last_name", nullable = false, length = 100)
    private String paternalLastName;

    @Column(name = "maternal_last_name", length = 100)
    private String maternalLastName;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 150)
    private String position;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.active == null) {
            this.active = true;
        }
    }
}