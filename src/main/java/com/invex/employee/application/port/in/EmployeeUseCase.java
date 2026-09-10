package com.invex.employee.application.port.in;

import com.invex.employee.domain.model.Employee;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;

import java.util.List;

public interface EmployeeUseCase {

    List<Employee> findAll();

    Employee findById(Long id);

    List<Employee> create(List<Employee> employees);

    Employee update(Long id, UpdateEmployeeCommand employee);

    void delete(Long id);

    List<Employee> searchByName(String name);
}