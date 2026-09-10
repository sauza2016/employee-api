package com.invex.employee.infrastructure.adapter.in.rest;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeUpdateRequest;
import com.invex.employee.infrastructure.adapter.in.rest.dto.UpdateEmployeeCommand;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invex.employee.application.exception.EmployeeNotFoundException;
import com.invex.employee.application.port.in.EmployeeUseCase;
import com.invex.employee.domain.model.Employee;
import com.invex.employee.domain.model.Gender;
import com.invex.employee.infrastructure.adapter.in.rest.dto.EmployeeCreateRequest;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeUseCase employeeUseCase;

    @Test
    void shouldReturnAllEmployees() throws Exception {

        Employee employee = createEmployee();

        when(employeeUseCase.findAll())
                .thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].position")
                        .value("Software Engineer"))
                .andExpect(jsonPath("$[0].active").value(true));
    }
    
    @Test
    void shouldReturnEmployeeById() throws Exception {

        Employee employee = createEmployee();

        when(employeeUseCase.findById(1L))
                .thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.middleName").value("Michael"))
                .andExpect(jsonPath("$.paternalLastName").value("Smith"))
                .andExpect(jsonPath("$.position").value("Software Engineer"))
                .andExpect(jsonPath("$.active").value(true));

        verify(employeeUseCase).findById(1L);
    }
    
    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {

        when(employeeUseCase.findById(99L))
                .thenThrow(new EmployeeNotFoundException(99L));

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("EMPLOYEE_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 99"));

        verify(employeeUseCase).findById(99L);
    }
    
    @Test
    void shouldCreateEmployees() throws Exception {

        EmployeeCreateRequest request = new EmployeeCreateRequest();

        request.setFirstName("John");
        request.setMiddleName("Michael");
        request.setPaternalLastName("Smith");
        request.setMaternalLastName("Brown");
        request.setAge(35);
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1991, 5, 10));
        request.setPosition("Software Engineer");
        request.setActive(true);

        Employee createdEmployee = createEmployee();

        when(employeeUseCase.create(anyList()))
                .thenReturn(List.of(createdEmployee));

        mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        List.of(request)
                                )
                        )
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].position")
                        .value("Software Engineer"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(employeeUseCase).create(anyList());
    }
    
    @Test
    void shouldReturnBadRequestWhenCreatingInvalidEmployee() throws Exception {

        EmployeeCreateRequest request = new EmployeeCreateRequest();

        request.setFirstName("");
        request.setPaternalLastName("Smith");
        request.setAge(35);
        request.setGender(Gender.MALE);
        request.setBirthDate(LocalDate.of(1991, 5, 10));
        request.setPosition("Software Engineer");
        request.setActive(true);

        mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        List.of(request)
                                )
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(employeeUseCase, never())
                .create(anyList());
    }
    
    @Test
    void shouldUpdateEmployee() throws Exception {

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        request.setPosition("Technical Lead");
        request.setActive(false);

        Employee updatedEmployee = createEmployee();
        updatedEmployee.setPosition("Technical Lead");
        updatedEmployee.setActive(false);

        when(employeeUseCase.update(
                eq(1L),
                any(UpdateEmployeeCommand.class)
        )).thenReturn(updatedEmployee);

        mockMvc.perform(
                put("/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.position").value("Technical Lead"))
                .andExpect(jsonPath("$.active").value(false));

        verify(employeeUseCase)
                .update(
                        eq(1L),
                        any(UpdateEmployeeCommand.class)
                );
    }
    
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingEmployee() throws Exception {

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();
        request.setPosition("Technical Lead");

        when(employeeUseCase.update(
                eq(99L),
                any(UpdateEmployeeCommand.class)
        )).thenThrow(new EmployeeNotFoundException(99L));

        mockMvc.perform(
                put("/employees/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("EMPLOYEE_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 99"));

        verify(employeeUseCase).update(
                eq(99L),
                any(UpdateEmployeeCommand.class)
        );
    }
    
    @Test
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {

        EmployeeUpdateRequest request = new EmployeeUpdateRequest();

        request.setPosition("A".repeat(151));

        mockMvc.perform(
                put("/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(employeeUseCase, never())
                .update(
                        anyLong(),
                        any(UpdateEmployeeCommand.class)
                );
    }
    
    @Test
    void shouldDeleteEmployee() throws Exception {

        mockMvc.perform(
                delete("/employees/{id}", 1L)
        )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(employeeUseCase).delete(1L);
    }
    
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingEmployee() throws Exception {

        doThrow(new EmployeeNotFoundException(99L))
                .when(employeeUseCase)
                .delete(99L);

        mockMvc.perform(
                delete("/employees/{id}", 99L)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code")
                        .value("EMPLOYEE_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 99"));

        verify(employeeUseCase).delete(99L);
    }
    
    @Test
    void shouldSearchEmployeesByName() throws Exception {

        Employee employee = createEmployee();

        when(employeeUseCase.searchByName("Joh"))
                .thenReturn(List.of(employee));

        mockMvc.perform(
                get("/employees/search")
                        .param("name", "Joh")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].position")
                        .value("Software Engineer"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(employeeUseCase).searchByName("Joh");
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
                LocalDateTime.of(2026, 9, 9, 10, 0),
                true
        );
    }
}