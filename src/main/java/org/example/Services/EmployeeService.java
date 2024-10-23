package org.example.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.DTOs.EmployeeDTO;
import org.example.Entities.Employee;
import org.example.Mappers.EmployeeMapper;
import org.example.Repositories.DepartmentRepository;
import org.example.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//basically logic-a na prilojenieto
//izpolzwa opredeleni za service-a repo-ta za da wzeme data ot db-tata za prerabotka

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    private final DepartmentRepository departmentRepo;

    public List<Employee> findAll(){
        return employeeRepo.findAll();
    }
    public Employee getEmployeeById(Long id){
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
    }
    public Employee saveEmployee(EmployeeDTO dto){
        Optional<Employee> dbObject = employeeRepo.findEmployeeByEmployeeNumber(dto.employeeNumber());

        Long id;
        if(dbObject.isPresent()){
            id = dbObject.get().getId();
        }
        else{
            id = null;
        }
        //na celiq goren if ima eqivalent expression
        //id = dbObject.map(Employee::getId).orElse(null);

        var employee = employeeMapper.convertDtoToEntity(dto, id);
        return employeeRepo.saveAndFlush(employee);
    }
    public Employee updateEmployeeData(Long id, EmployeeDTO dto){
        var employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
        employee = employeeMapper.convertDtoToEntity(dto, id);
        return employeeRepo.saveAndFlush(employee);
    }
    public Employee changeDepartment(Long id, Long depId){
        var employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
        var department = departmentRepo.findById(depId)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + depId));

        employee.setDepartment(department);

        return employeeRepo.saveAndFlush(employee);
    }
    public void deleteEmployee(Long id){
        var employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
        employeeRepo.delete(employee);
    }
}