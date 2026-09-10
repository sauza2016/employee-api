package com.invex.employee.infrastructure.adapter.out.persistence;

import com.invex.employee.application.port.out.EmployeeRepositoryPort;
import com.invex.employee.domain.model.Employee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmployeeJpaAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository repository;

    public EmployeeJpaAdapter(EmployeeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findAll() {
        return repository.findAll()
                .stream()
                .map(EmployeePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return repository.findById(id)
                .map(EmployeePersistenceMapper::toDomain);
    }

    @Override
    public List<Employee> saveAll(List<Employee> employees) {

        List<EmployeeEntity> entities = employees.stream()
                .map(EmployeePersistenceMapper::toEntity)
                .collect(Collectors.toList());

        return repository.saveAll(entities)
                .stream()
                .map(EmployeePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Employee save(Employee employee) {

        EmployeeEntity entity =
                EmployeePersistenceMapper.toEntity(employee);

        EmployeeEntity savedEntity =
                repository.save(entity);

        return EmployeePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Employee> searchByName(String name) {
        return repository.searchByName(name)
                .stream()
                .map(EmployeePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}