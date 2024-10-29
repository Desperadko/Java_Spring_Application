package org.example.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.AllArgsConstructor;
import org.example.DTOs.ProjectDTO;
import org.example.Entities.Employee;
import org.example.Entities.Project;
import org.example.Mappers.ProjectMapper;
import org.example.Repositories.EmployeeRepository;
import org.example.Repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {

    ProjectRepository projectRepo;
    ProjectMapper projectMapper;

    EmployeeRepository employeeRepo;

    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepo.findAll(pageable);
    }
    public Project getProjectById(Long projectId){
        return getProject(projectId);
    }
    public Project addProject(ProjectDTO projectDTO){
        var projectName = projectDTO.name();

        var dbObject = projectRepo.findProjectByName(projectName);
        if(dbObject.isPresent()){
            throw new IllegalStateException("Project with project name: '" + projectName + "' already exists. " +
                    "Choose another name..");
        }

        return saveProjectDtoToDatabase(projectDTO, null);
    }
    public Project updateProjectMetadata(Long projectId, ProjectDTO projectDTO){
        if(!projectRepo.existsById(projectId))
            throw new EntityNotFoundException("Project ID: " + projectId);

        return saveProjectDtoToDatabase(projectDTO, projectId);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProject(Long projectId){
        var project = getProject(projectId);
        var employees = getEmployeesAssignedToProject(project);

        if(!employees.isEmpty())
            throw new IllegalStateException("Project with project ID: '" + projectId + "' " +
                    "should be emptied out of employees before deletion..");

        //to be worked on...
        //employees.forEach(employee -> employee.getProjects().remove(project));
        //employeeRepo.saveAllAndFlush(employees);

        projectRepo.delete(project);
    }

    public Page<Employee> getEmployeesAssignedToProject(Long projectId, Pageable pageable){
        var project = getProject(projectId);

        return getEmployeesAssignedToProject(project, pageable);
    }
    public Employee assignEmployeeToProject(Long projectId, Long employeeId){
        var project = getProject(projectId);
        var employee = getEmployee(employeeId);

        employee.getProjects().add(project);

        return employeeRepo.saveAndFlush(employee);
    }
    public List<Employee> updateEmployeeListForProject(Long projectId, List<Long> employeeIds){
        var project = getProject(projectId);
        var currentEmployeesAssignedToProject = getEmployeesAssignedToProject(project);

        currentEmployeesAssignedToProject.forEach(employee -> employee.getProjects().remove(project));
        employeeRepo.saveAll(currentEmployeesAssignedToProject);

        var employeesToAssign = getEmployeesFromListOfIds(employeeIds);
        employeesToAssign.forEach(employee -> employee.getProjects().add(project));
        employeeRepo.saveAllAndFlush(employeesToAssign);

        return employeesToAssign;
    }
    public void removeEmployeeFromProject(Long projectId, Long employeeId){
        var project = getProject(projectId);
        var employee = getEmployee(employeeId);

        employee.getProjects().remove(project);

        employeeRepo.saveAndFlush(employee);
    }

    //pomo6ni funkcii
    private Project getProject(Long projectId) {
        return projectRepo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project ID: " + projectId));
    }
    private Employee getEmployee(Long employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee ID: " + employeeId));
    }
    private Project saveProjectDtoToDatabase(ProjectDTO projectDTO, Long projectId) {
        var project = projectMapper.convertDtoToEntity(projectDTO, projectId);
        return projectRepo.saveAndFlush(project);
    }
    private List<Employee> getEmployeesAssignedToProject(Project project){
        return employeeRepo.findEmployeesByProject(project)
                .orElseThrow(() -> new EntityNotFoundException("Employees not found with project ID:" + project.getId()));
    }
    private Page<Employee> getEmployeesAssignedToProject(Project project, Pageable pageable){
        return employeeRepo.findEmployeesByProject(project, pageable);
    }
    private List<Employee> getEmployeesFromListOfIds(List<Long> employeeList) {
        return employeeRepo.findAllByIdIn(employeeList)
                .orElseThrow(() -> {
                    String employeeListToStr = Arrays.stream(employeeList.toArray())
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "));
                    return new EntityNotFoundException("Couldn't find employees with the given IDs: " + employeeListToStr);
                });
    }
}
