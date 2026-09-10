package com.invex.employee.infrastructure.adapter.out.persistence;

import com.invex.employee.domain.model.Gender;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeJpaRepositoryTest {

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    @Test
    void shouldFindEmployeesByPartialName() {

        EmployeeEntity employee = createEmployeeEntity();

        EmployeeEntity saved =
                employeeJpaRepository.saveAndFlush(employee);

        List<EmployeeEntity> result =
                employeeJpaRepository.searchByName("Joh");

        assertEquals(1, result.size());

        EmployeeEntity found = result.get(0);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());

        assertEquals("John", found.getFirstName());
        assertEquals("Software Engineer", found.getPosition());
        assertTrue(found.getActive());
    }
    
    @Test
    void shouldFindEmployeesIgnoringCase() {

        EmployeeEntity employee = createEmployeeEntity();
        employeeJpaRepository.saveAndFlush(employee);

        List<EmployeeEntity> result =
                employeeJpaRepository.searchByName("jOh");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }
    
    @Test
    void shouldFindEmployeesByLastName() {

        EmployeeEntity employee = createEmployeeEntity();
        employeeJpaRepository.saveAndFlush(employee);

        List<EmployeeEntity> result =
                employeeJpaRepository.searchByName("Smi");

        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getPaternalLastName());
    }
    
    @Test
    void shouldGenerateIdAndCreatedAtWhenSavingEmployee() {

        EmployeeEntity employee = createEmployeeEntity();

        EmployeeEntity saved =
                employeeJpaRepository.saveAndFlush(employee);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
    }
    
    @Test
    void shouldReturnEmptyListWhenNoEmployeeMatches() {

        EmployeeEntity employee = createEmployeeEntity();
        employeeJpaRepository.saveAndFlush(employee);

        List<EmployeeEntity> result =
                employeeJpaRepository.searchByName("Carlos");

        assertTrue(result.isEmpty());
    }
    
    
    
    private EmployeeEntity createEmployeeEntity() {

        EmployeeEntity employee = new EmployeeEntity();

        employee.setFirstName("John");
        employee.setMiddleName("Michael");
        employee.setPaternalLastName("Smith");
        employee.setMaternalLastName("Brown");
        employee.setAge(35);
        employee.setGender(Gender.MALE);
        employee.setBirthDate(LocalDate.of(1991, 5, 10));
        employee.setPosition("Software Engineer");
        employee.setActive(true);

        return employee;
    }
}