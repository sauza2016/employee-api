package com.invex.employee.infrastructure.adapter.out.persistence;

import com.invex.employee.domain.model.Employee;

public final class EmployeePersistenceMapper {

    private EmployeePersistenceMapper() {
    }

    public static EmployeeEntity toEntity(Employee employee) {

        EmployeeEntity entity = new EmployeeEntity();

        entity.setId(employee.getId());
        entity.setFirstName(employee.getFirstName());
        entity.setMiddleName(employee.getMiddleName());
        entity.setPaternalLastName(employee.getPaternalLastName());
        entity.setMaternalLastName(employee.getMaternalLastName());
        entity.setAge(employee.getAge());
        entity.setGender(employee.getGender());
        entity.setBirthDate(employee.getBirthDate());
        entity.setPosition(employee.getPosition());
        entity.setCreatedAt(employee.getCreatedAt());
        entity.setActive(employee.isActive());

        return entity;
    }

    public static Employee toDomain(EmployeeEntity entity) {

        return new Employee(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getPaternalLastName(),
                entity.getMaternalLastName(),
                entity.getAge(),
                entity.getGender(),
                entity.getBirthDate(),
                entity.getPosition(),
                entity.getCreatedAt(),
                entity.getActive()
        );
    }
}