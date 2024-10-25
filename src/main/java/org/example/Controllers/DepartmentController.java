package org.example.Controllers;

import lombok.AllArgsConstructor;
import org.example.DTOs.DepartmentDTO;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.example.Services.DepartmentService;
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
    public ResponseEntity<List<Department>> getAllDepartments(){
        var departments = departmentService.findAllDepartments();
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
    public ResponseEntity<List<Employee>> getEmployeesFromDepartment(@PathVariable Long departmentId){
        var employees = departmentService.findEmployees(departmentId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> addEmployeeToDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        departmentService.addEmployeeToDepartment(departmentId, employeeId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{departmentId}/employees")
    public ResponseEntity<List<Employee>> updateEmployeeListForDepartment(@PathVariable Long departmentId, @RequestParam List<Long> employeeList){
        var employees = departmentService.updateEmployeeListForDepartment(departmentId, employeeList);
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> removeEmployeeFromDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        var employee = departmentService.removeEmployeeFromDepartment(departmentId, employeeId);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
}
