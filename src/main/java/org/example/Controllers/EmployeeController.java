package org.example.Controllers;

import lombok.AllArgsConstructor;
import org.example.Entities.Employee;
import org.example.Services.EmployeeService;
import org.example.DTOs.EmployeeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//handle-wa http zaqwkite.
//expose-wa functsionalnostta na prilojenieto pred usera.
//izwikwa funkciite na servicite, kat polzwa podadenite argumenti ot usera (@PathVariable, @RequestBody ...)
//pri polzwane na servica na prilojenieto.
@RestController
@AllArgsConstructor
@RequestMapping(value = "/employee") //handlewa samo pri /employee
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees(){
        var employees = employeeService.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<Employee> getEmployeeByID(@PathVariable Long employeeId){
        var employee = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        var employee = employeeService.addEmployee(employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO){
        var employee = employeeService.updateEmployeeData(employeeId, employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{employeeId}/department")
    public ResponseEntity<Employee> changeDepartment(@PathVariable Long employeeId, @RequestParam Long departmentId){
        employeeService.changeDepartment(employeeId, departmentId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{employeeId}/department")
    public ResponseEntity<?> removeEmployeeFromDepartment(@PathVariable Long employeeId){
        employeeService.removeEmployeeFromDepartment(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value = "/{employeeId}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
