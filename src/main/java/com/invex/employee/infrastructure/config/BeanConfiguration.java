package com.invex.employee.infrastructure.config;

import com.invex.employee.application.port.in.EmployeeUseCase;
import com.invex.employee.application.port.out.EmployeeRepositoryPort;
import com.invex.employee.application.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public EmployeeUseCase employeeUseCase(
            EmployeeRepositoryPort employeeRepositoryPort) {

        return new EmployeeService(employeeRepositoryPort);
    }
}