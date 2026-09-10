package com.invex.employee.application.port.out;

import com.invex.employee.domain.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryPort {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    List<Employee> saveAll(List<Employee> employees);

    Employee save(Employee employee);

    void deleteById(Long id);

    List<Employee> searchByName(String name);
}