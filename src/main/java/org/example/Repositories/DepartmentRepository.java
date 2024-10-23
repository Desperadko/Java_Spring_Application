package org.example.Repositories;

import org.example.Entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    public Optional<Department> findDepartmentByName(String name);
}
