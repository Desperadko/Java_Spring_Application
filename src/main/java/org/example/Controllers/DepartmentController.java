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
        var departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Department> getDepartmentByID(@PathVariable Long id){
        var department = departmentService.findDepartmentById(id);
        return ResponseEntity.ok(department);
    }
    @PostMapping()
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDTO dto){
        var department = departmentService.saveDepartment(dto);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Department> updateDepartmentData(@PathVariable Long id, @RequestBody DepartmentDTO dto){
        var department = departmentService.updateDepartmentData(id, dto);
        return new ResponseEntity<>(department, HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //vklu4wa6ti i employees
    @GetMapping(value = "{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesFromDepartment(@PathVariable Long id){
        var employees = departmentService.findEmployees(id);
        return ResponseEntity.ok(employees);
    }
    @PatchMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> addEmployeeToDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        departmentService.addEmployeeToDepartment(departmentId, employeeId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Employee> removeEmployeeFromDepartment(@PathVariable Long departmentId, @PathVariable Long employeeId){
        var employee = departmentService.removeEmployeeFromDepartment(departmentId, employeeId);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{departmentId}/employees")
    public ResponseEntity<List<Employee>> updateEmployeeListForDepartment(@PathVariable Long departmentId, @RequestParam List<Long> employeeList){
        var employees = departmentService.updateEmployeeListForDepartment(departmentId, employeeList);
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }
}
