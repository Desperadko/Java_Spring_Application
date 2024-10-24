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
        return ResponseEntity.ok(employees);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> getEmployeeByID(@PathVariable Long id){
        var employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO dto){
        var employee = employeeService.saveEmployee(dto);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO dto){
        var employee = employeeService.updateEmployeeData(id, dto);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }
    @PatchMapping(value = "/{id}/department")
    public ResponseEntity<Employee> changeDepartment(@PathVariable Long id, @RequestParam Long depId){
        employeeService.changeDepartment(id, depId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @DeleteMapping(value = "/{id}/department")
    public ResponseEntity<?> removeEmployeeFromDepartment(@PathVariable Long id){
        employeeService.removeEmployeeFromDepartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
