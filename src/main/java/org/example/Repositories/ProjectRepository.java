package org.example.Repositories;

import org.example.Entities.Department;
import org.example.Entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public Page<Project> findAll(Pageable pageable);
    public Optional<Project> findProjectByName(String projectName);
}
