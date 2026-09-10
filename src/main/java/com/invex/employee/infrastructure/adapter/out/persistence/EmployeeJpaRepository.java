package com.invex.employee.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeJpaRepository
        extends JpaRepository<EmployeeEntity, Long> {

    @Query("""
        SELECT e
        FROM EmployeeEntity e
        WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(e.middleName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(e.paternalLastName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(e.maternalLastName) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    List<EmployeeEntity> searchByName(@Param("name") String name);
}