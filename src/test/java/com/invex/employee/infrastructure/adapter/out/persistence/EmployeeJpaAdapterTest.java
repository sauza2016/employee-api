package com.invex.employee.infrastructure.adapter.out.persistence;

import com.invex.employee.domain.model.Employee;
import com.invex.employee.domain.model.Gender;

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
class EmployeeJpaAdapterTest {

    @Mock
    private EmployeeJpaRepository employeeJpaRepository;

    private EmployeeJpaAdapter employeeJpaAdapter;

    @BeforeEach
    void setUp() {
        employeeJpaAdapter =
                new EmployeeJpaAdapter(employeeJpaRepository);
    }

    @Test
    void shouldFindAllEmployees() {

        EmployeeEntity entity = createEmployeeEntity();

        when(employeeJpaRepository.findAll())
                .thenReturn(List.of(entity));

        List<Employee> result =
                employeeJpaAdapter.findAll();

        assertEquals(1, result.size());

        Employee employee = result.get(0);

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getPaternalLastName());
        assertEquals("Software Engineer", employee.getPosition());
        assertTrue(employee.isActive());

        verify(employeeJpaRepository).findAll();
    }

    @Test
    void shouldFindEmployeeById() {

        EmployeeEntity entity = createEmployeeEntity();

        when(employeeJpaRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        Optional<Employee> result =
                employeeJpaAdapter.findById(1L);

        assertTrue(result.isPresent());

        Employee employee = result.get();

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getPaternalLastName());
        assertEquals("Software Engineer", employee.getPosition());
        assertTrue(employee.isActive());

        verify(employeeJpaRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyOptionalWhenEmployeeDoesNotExist() {

        when(employeeJpaRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Employee> result =
                employeeJpaAdapter.findById(99L);

        assertTrue(result.isEmpty());

        verify(employeeJpaRepository).findById(99L);
    }

    @Test
    void shouldSaveEmployee() {

        Employee employee = createEmployee();
        EmployeeEntity savedEntity = createEmployeeEntity();

        when(employeeJpaRepository.save(any(EmployeeEntity.class)))
                .thenReturn(savedEntity);

        Employee result =
                employeeJpaAdapter.save(employee);

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Michael", result.getMiddleName());
        assertEquals("Smith", result.getPaternalLastName());
        assertEquals("Software Engineer", result.getPosition());
        assertTrue(result.isActive());

        verify(employeeJpaRepository)
                .save(any(EmployeeEntity.class));
    }

    @Test
    void shouldSaveAllEmployees() {

        Employee employee = createEmployee();
        EmployeeEntity entity = createEmployeeEntity();

        when(employeeJpaRepository.saveAll(anyList()))
                .thenReturn(List.of(entity));

        List<Employee> result =
                employeeJpaAdapter.saveAll(List.of(employee));

        assertEquals(1, result.size());

        Employee savedEmployee = result.get(0);

        assertEquals(1L, savedEmployee.getId());
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Software Engineer",
                savedEmployee.getPosition());

        verify(employeeJpaRepository)
                .saveAll(anyList());
    }

    @Test
    void shouldDeleteEmployeeById() {

        employeeJpaAdapter.deleteById(1L);

        verify(employeeJpaRepository).deleteById(1L);
    }

    @Test
    void shouldSearchEmployeesByName() {

        EmployeeEntity entity = createEmployeeEntity();

        when(employeeJpaRepository.searchByName("Joh"))
                .thenReturn(List.of(entity));

        List<Employee> result =
                employeeJpaAdapter.searchByName("Joh");

        assertEquals(1, result.size());

        Employee employee = result.get(0);

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith",
                employee.getPaternalLastName());

        verify(employeeJpaRepository)
                .searchByName("Joh");
    }

    @Test
    void shouldReturnEmptyListWhenSearchHasNoResults() {

        when(employeeJpaRepository.searchByName("Carlos"))
                .thenReturn(List.of());

        List<Employee> result =
                employeeJpaAdapter.searchByName("Carlos");

        assertTrue(result.isEmpty());

        verify(employeeJpaRepository)
                .searchByName("Carlos");
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
                        2026, 9, 9, 10, 0
                ),
                true
        );
    }

    private EmployeeEntity createEmployeeEntity() {

        EmployeeEntity employee =
                new EmployeeEntity();

        employee.setId(1L);
        employee.setFirstName("John");
        employee.setMiddleName("Michael");
        employee.setPaternalLastName("Smith");
        employee.setMaternalLastName("Brown");
        employee.setAge(35);
        employee.setGender(Gender.MALE);
        employee.setBirthDate(
                LocalDate.of(1991, 5, 10)
        );
        employee.setPosition(
                "Software Engineer"
        );
        employee.setCreatedAt(
                LocalDateTime.of(
                        2026, 9, 9, 10, 0
                )
        );
        employee.setActive(true);

        return employee;
    }
}