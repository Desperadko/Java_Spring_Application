package org.example.Controllers;

import lombok.AllArgsConstructor;
import org.example.DTOs.DepartmentDTO;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.example.Services.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    //department specific
    @GetMapping()
    public ResponseEntity<Page<Department>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        var departments = departmentService.findAllDepartments(PageRequest.of(page, size));
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
    @GetMapping(value = "/{departmentId}")
    public ResponseEntity<Department> getDepartmentByID(@PathVariable Long departmentId){
        var department = departmentService.findDepartmentById(departmentId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDTO departmentDTO){
        var department = departmentService.addDepartment(departmentDTO);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }
    @PatchMapping(value = "/{departmentId}")
    public ResponseEntity<Department> updateDepartmentData(@PathVariable Long departmentId, @RequestBody DepartmentDTO departmentDTO){
        var department = departmentService.updateDepartmentMetadata(departmentId, departmentDTO);
        return new ResponseEntity<>(department, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{departmentId}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable Long departmentId){
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //vklu4wa6ti i employees
    @GetMapping(value = "{departmentId}/employees")
    public ResponseEntity<Page<Employee>> getEmployeesFromDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        var employees = departmentService.getEmployeesFromDepartment(departmentId, PageRequest.of(page, size));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> addEmployeeToDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        departmentService.addEmployeeToDepartment(departmentId, employeeId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{departmentId}/employees")
    public ResponseEntity<List<Employee>> updateEmployeeListForDepartment(@PathVariable Long departmentId, @RequestParam List<Long> employeeIds){
        var employees = departmentService.updateEmployeeListForDepartment(departmentId, employeeIds);
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> removeEmployeeFromDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        departmentService.removeEmployeeFromDepartment(departmentId, employeeId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
