package com.invex.employee.application.service;

import com.invex.employee.application.exception.EmployeeNotFoundException;
import com.invex.employee.application.port.in.EmployeeUseCase;
import com.invex.employee.application.port.out.EmployeeRepositoryPort;
import com.invex.employee.domain.model.Employee;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;

import java.util.List;

public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        this.employeeRepositoryPort = employeeRepositoryPort;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepositoryPort.findAll();
    }

    @Override
    public Employee findById(Long id) {
    	return employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public List<Employee> create(List<Employee> employees) {
        return employeeRepositoryPort.saveAll(employees);
    }

    @Override
    public Employee update(Long id, UpdateEmployeeCommand command) {

        Employee existingEmployee = employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (command.getFirstName() != null) {
            existingEmployee.setFirstName(command.getFirstName());
        }

        if (command.getMiddleName() != null) {
            existingEmployee.setMiddleName(command.getMiddleName());
        }

        if (command.getPaternalLastName() != null) {
            existingEmployee.setPaternalLastName(command.getPaternalLastName());
        }

        if (command.getMaternalLastName() != null) {
            existingEmployee.setMaternalLastName(command.getMaternalLastName());
        }

        if (command.getAge() != null) {
            existingEmployee.setAge(command.getAge());
        }

        if (command.getGender() != null) {
            existingEmployee.setGender(command.getGender());
        }

        if (command.getBirthDate() != null) {
            existingEmployee.setBirthDate(command.getBirthDate());
        }

        if (command.getPosition() != null) {
            existingEmployee.setPosition(command.getPosition());
        }

        if (command.getActive() != null) {
            existingEmployee.setActive(command.getActive());
        }

        return employeeRepositoryPort.save(existingEmployee);
    }

    @Override
    public void delete(Long id) {

    	employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepositoryPort.deleteById(id);
    }

    @Override
    public List<Employee> searchByName(String name) {
        return employeeRepositoryPort.searchByName(name);
    }
}