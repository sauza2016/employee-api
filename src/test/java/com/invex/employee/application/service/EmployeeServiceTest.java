package com.invex.employee.application.service;

import com.invex.employee.application.exception.EmployeeNotFoundException;
import com.invex.employee.application.port.out.EmployeeRepositoryPort;
import com.invex.employee.domain.model.Employee;
import com.invex.employee.domain.model.Gender;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepositoryPort employeeRepositoryPort;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService =
                new EmployeeService(employeeRepositoryPort);
    }

    @Test
    void shouldReturnAllEmployees() {

        Employee employee1 = createEmployee();

        Employee employee2 = createEmployee();
        employee2.setId(2L);
        employee2.setFirstName("Alice");

        when(employeeRepositoryPort.findAll())
                .thenReturn(List.of(employee1, employee2));

        List<Employee> result =
                employeeService.findAll();

        assertEquals(2, result.size());

        verify(employeeRepositoryPort).findAll();
    }

    @Test
    void shouldReturnEmployeeWhenEmployeeExists() {

        Employee employee = createEmployee();

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(Optional.of(employee));

        Employee result =
                employeeService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());

        verify(employeeRepositoryPort)
                .findById(1L);
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.findById(99L)
        );

        verify(employeeRepositoryPort)
                .findById(99L);
    }

    @Test
    void shouldCreateEmployees() {

        Employee employee1 = createEmployee();
        employee1.setId(null);

        Employee employee2 = createEmployee();
        employee2.setId(null);
        employee2.setFirstName("Alice");

        List<Employee> request =
                List.of(employee1, employee2);

        Employee savedEmployee1 = createEmployee();

        Employee savedEmployee2 = createEmployee();
        savedEmployee2.setId(2L);
        savedEmployee2.setFirstName("Alice");

        when(employeeRepositoryPort.saveAll(request))
                .thenReturn(
                        List.of(
                                savedEmployee1,
                                savedEmployee2
                        )
                );

        List<Employee> result =
                employeeService.create(request);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(employeeRepositoryPort)
                .saveAll(request);
    }

    @Test
    void shouldUpdateProvidedFields() {

        Employee existingEmployee = createEmployee();
        existingEmployee.setActive(true);

        UpdateEmployeeCommand command = new UpdateEmployeeCommand();

        command.setPosition("Technical Lead");
        command.setActive(false);

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepositoryPort.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.update(
                1L,
                command
        );

        assertEquals("John", result.getFirstName());
        assertEquals("Technical Lead", result.getPosition());
        assertFalse(result.isActive());

        verify(employeeRepositoryPort).findById(1L);
        verify(employeeRepositoryPort).save(existingEmployee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingEmployee() {

        UpdateEmployeeCommand command =
                new UpdateEmployeeCommand();

        command.setPosition("Technical Lead");

        when(employeeRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.update(
                        99L,
                        command
                )
        );

        verify(employeeRepositoryPort, never())
                .save(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployeeWhenEmployeeExists() {

        Employee employee =
                createEmployee();

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(
                        Optional.of(employee)
                );

        employeeService.delete(1L);

        verify(employeeRepositoryPort)
                .findById(1L);

        verify(employeeRepositoryPort)
                .deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {

        when(employeeRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.delete(99L)
        );

        verify(employeeRepositoryPort, never())
                .deleteById(anyLong());
    }

    @Test
    void shouldReturnEmployeesMatchingName() {

        Employee employee =
                createEmployee();

        when(employeeRepositoryPort.searchByName("Joh"))
                .thenReturn(List.of(employee));

        List<Employee> result =
                employeeService.searchByName("Joh");

        assertEquals(1, result.size());
        assertEquals(
                "John",
                result.get(0).getFirstName()
        );

        verify(employeeRepositoryPort)
                .searchByName("Joh");
    }

    private Employee createEmployee() {

        return new Employee(
                1L,
                "John",
                "Michael",
                "Smith",
                "Brown",
                35,
                Gender.MALE,
                LocalDate.of(1991, 5, 10),
                "Software Engineer",
                LocalDateTime.of(
                        2026,
                        9,
                        9,
                        10,
                        0
                ),
                true
        );
    }
    @Test
    void shouldPreserveActiveWhenNotProvided() {

        Employee existingEmployee = createEmployee();
        existingEmployee.setActive(true);

        UpdateEmployeeCommand command = new UpdateEmployeeCommand();
        command.setPosition("Technical Lead");

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepositoryPort.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.update(1L, command);

        assertEquals("Technical Lead", result.getPosition());
        assertTrue(result.isActive());

        verify(employeeRepositoryPort).save(existingEmployee);
    }
    
    @Test
    void shouldUpdateOnlyProvidedFields() {

        Employee existingEmployee = createEmployee();

        UpdateEmployeeCommand command = new UpdateEmployeeCommand();
        command.setPosition("Technical Lead");

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepositoryPort.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.update(1L, command);

        assertEquals("Technical Lead", result.getPosition());

        assertEquals("John", result.getFirstName());
        assertEquals("Michael", result.getMiddleName());
        assertEquals("Smith", result.getPaternalLastName());
        assertTrue(result.isActive());

        verify(employeeRepositoryPort).findById(1L);
        verify(employeeRepositoryPort).save(existingEmployee);
    }
}