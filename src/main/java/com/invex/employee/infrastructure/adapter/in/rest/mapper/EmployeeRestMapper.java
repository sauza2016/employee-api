package com.invex.employee.infrastructure.adapter.in.rest.mapper;

import com.invex.employee.domain.model.Employee;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeCreateRequest;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeResponse;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeUpdateRequest;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;

public final class EmployeeRestMapper {

    private EmployeeRestMapper() {
    }

    public static Employee toDomain(EmployeeCreateRequest request) {

        return new Employee(
                null,
                request.getFirstName(),
                request.getMiddleName(),
                request.getPaternalLastName(),
                request.getMaternalLastName(),
                request.getAge(),
                request.getGender(),
                request.getBirthDate(),
                request.getPosition(),
                null,
                request.getActive()
        );
    }

    public static UpdateEmployeeCommand toCommand(
            EmployeeUpdateRequest request) {

        return new UpdateEmployeeCommand(
                request.getFirstName(),
                request.getMiddleName(),
                request.getPaternalLastName(),
                request.getMaternalLastName(),
                request.getAge(),
                request.getGender(),
                request.getBirthDate(),
                request.getPosition(),
                request.getActive()
        );
    }

    public static EmployeeResponse toResponse(Employee  employee) {

        return new EmployeeResponse(
        		employee.getId(),
                employee.getFirstName(),
                employee.getMiddleName(),
                employee.getPaternalLastName(),
                employee.getMaternalLastName(),
                employee.getAge(),
                employee.getGender(),
                employee.getBirthDate(),
                employee.getPosition(),
                null,
                employee.isActive()
        );
    }
}