package org.example.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.DTOs.EmployeeDTO;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.example.Mappers.EmployeeMapper;
import org.example.Repositories.DepartmentRepository;
import org.example.Repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Employee> findAll(Pageable pageable){
        return employeeRepo.findAll(pageable);
    }
    public Employee getEmployeeById(Long employeeId){
        return getEmployee(employeeId);
    }
    public Employee addEmployee(EmployeeDTO employeeDTO){
        var employeeNumber = employeeDTO.employeeNumber();

        Optional<Employee> dbObject = employeeRepo.findEmployeeByEmployeeNumber(employeeNumber);
        if(dbObject.isPresent()){
            throw new IllegalStateException("Employee with employee number '" + employeeNumber + "' already exists. " +
                    "Check if you don't want to UPDATE said employee, otherwise choose another employee number.");
        }

        return saveEmployeeDtoToDatabase(employeeDTO, null);
    }
    public Employee updateEmployeeData(Long employeeId, EmployeeDTO employeeDTO){
        if(!employeeRepo.existsById(employeeId))
            throw new EntityNotFoundException("Employee ID: " + employeeId);
        return saveEmployeeDtoToDatabase(employeeDTO, employeeId);
    }
    public void changeDepartment(Long employeeId, Long departmentId){
        var department = getDepartment(departmentId);

        employeeRepo.updateEmployeeDepartment(employeeId, department);
    }
    public void removeEmployeeFromDepartment(Long employeeId) {
        employeeRepo.removeEmployeeFromDepartment(employeeId);
    }
    public void deleteEmployee(Long employeeId){
        var employee = getEmployee(employeeId);
        employeeRepo.delete(employee);
    }

    //pomo6ni funckii
    private Employee getEmployee(Long employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee ID: " + employeeId));
    }
    private Department getDepartment(Long departmentId) {
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + departmentId));
    }
    private Employee saveEmployeeDtoToDatabase(EmployeeDTO employeeDTO, Long id) {
        var employee = employeeMapper.convertDtoToEntity(employeeDTO, id);
        return employeeRepo.saveAndFlush(employee);
    }
}