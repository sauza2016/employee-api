package com.invex.employee.infrastructure.config;

import com.invex.employee.application.port.in.EmployeeUseCase;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeUseCase employeeUseCase;

    @Test
    void shouldReturnUnauthorizedWhenAccessingEmployeesWithoutAuthentication()
            throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAccessToEmployeesWithValidCredentials()
            throws Exception {

        when(employeeUseCase.findAll())
                .thenReturn(List.of());

        mockMvc.perform(
                get("/employees")
                        .with(httpBasic("admin", "admin123"))
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAccessToHealthWithoutAuthentication()
            throws Exception {

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnApplicationHealth() throws Exception {

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldAllowAccessToApiDocsWithoutAuthentication()
            throws Exception {

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturnUnauthorizedWithInvalidCredentials()
            throws Exception {

        mockMvc.perform(
                get("/employees")
                        .with(httpBasic("admin", "wrongPassword"))
        )
                .andExpect(status().isUnauthorized());
    }
}