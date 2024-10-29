package org.example.Controllers;

import lombok.AllArgsConstructor;
import org.example.DTOs.ProjectDTO;
import org.example.Entities.Employee;
import org.example.Entities.Project;
import org.example.Services.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/project")
public class ProjectController {

    private final ProjectService projectService;

    //project specific
    @GetMapping()
    public ResponseEntity<Page<Project>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        var projects = projectService.getAllProjects(PageRequest.of(page, size));
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId){
        var project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO){
        var project = projectService.addProject(projectDTO);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }
    @PatchMapping(value = "/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody ProjectDTO projectDTO){
        var project = projectService.updateProjectMetadata(projectId, projectDTO);
        return new ResponseEntity<>(project, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Project> deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //vklu4wa6ti i employees
    @GetMapping(value = "{projectId}/employees")
    public ResponseEntity<Page<Employee>> getEmployeesAssignedToProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        var employees = projectService.getEmployeesAssignedToProject(projectId, PageRequest.of(page, size));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping(value = "{projectId}/employees/{employeeId}")
    public ResponseEntity<Employee> assignEmployeeToProject(@PathVariable Long projectId, @PathVariable Long employeeId){
        var employee = projectService.assignEmployeeToProject(projectId, employeeId);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{projectId}/employees")
    public ResponseEntity<List<Employee>> updateEmployeeListForProject(@PathVariable Long projectId, @RequestParam List<Long> employeeIds){
        var employees = projectService.updateEmployeeListForProject(projectId, employeeIds);
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<Employee> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId){
        projectService.removeEmployeeFromProject(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
