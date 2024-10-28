package org.example.Repositories;

import jakarta.validation.constraints.NotNull;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    public Page<Department> findAll(Pageable pageable);
    public Optional<Department> findDepartmentByName(String name);
}
