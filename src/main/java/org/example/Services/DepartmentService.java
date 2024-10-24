package org.example.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.DTOs.DepartmentDTO;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.example.Mappers.DepartmentMapper;
import org.example.Repositories.DepartmentRepository;
import org.example.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepo;
    private final DepartmentMapper departmentMapper;

    private final EmployeeRepository employeeRepo;

    public List<Department> findAll(){
        return departmentRepo.findAll();
    }
    public Department findDepartmentById(Long id){
        return departmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
    }
    public Department saveDepartment(DepartmentDTO dto){
        Optional<Department> dbObject = departmentRepo.findDepartmentByName(dto.name());

        Long id;
        if(dbObject.isPresent()){
            id = dbObject.get().getId();
        }
        else{
            id = null;
        }

        var department = departmentMapper.convertDtoToEntity(dto, id);
        return departmentRepo.saveAndFlush(department);
    }
    public Department updateDepartmentData(Long id, DepartmentDTO dto){
        var department = departmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));
        department = departmentMapper.convertDtoToEntity(dto, id);
        return departmentRepo.saveAndFlush(department);
    }
    public void deleteDepartment(Long id){
        var department = departmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id));

        var employees = employeeRepo.findEmployeesByDepartment(department)
                .orElseThrow(() -> new EntityNotFoundException("Employees not found with department ID: " + department.getId()));

        for(var employee : employees){
            employee.setDepartment(null);
            employeeRepo.save(employee);
        }
        employeeRepo.flush();

        departmentRepo.delete(department);
    }
    public List<Employee> findEmployees(Long id){
        var department = departmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + id));

        return employeeRepo.findEmployeesByDepartment(department)
                .orElseThrow(() -> new EntityNotFoundException("Employees not found with department ID: " + department.getId()));
    }

    public void addEmployeeToDepartment(Long departmentId, Long employeeId) {
        var department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + departmentId));
        employeeRepo.updateEmployeeDepartment(employeeId, department);
    }
    public void addEmployeeToDepartmentNative(Long departmentId, Long employeeId){
        employeeRepo.updateEmployeeDepartmentNative(employeeId, departmentId);
    }

    public Employee removeEmployeeFromDepartment(Long departmentId, Long employeeId) {
        var employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee ID: " + employeeId));

        int updatedRows = employeeRepo.removeEmployeeFromDepartmentIfExists(employeeId, departmentId);
        if (updatedRows == 0)
            return null;

        return employeeRepo.saveAndFlush(employee);
    }

    public List<Employee> updateEmployeeListForDepartment(Long departmentId, List<Long> employeeList) {
        var department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + departmentId));
        var currentEmployeesInDepartment = employeeRepo.findEmployeesByDepartment(department)
                .orElseThrow(() -> new EntityNotFoundException("Employees not found with department ID: " + department.getId()));

        for(var employee : currentEmployeesInDepartment){
            employee.setDepartment(null);
            employeeRepo.save(employee);
        }

        var employeesToAdd = employeeRepo.findAllByIdIn(employeeList)
                .orElseThrow(() -> {
                    String employeeListToStr = Arrays.stream(employeeList.toArray())
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "));
                    return new EntityNotFoundException("Couldn't find employees with the given IDs: " + employeeListToStr);
                });
        for(var employee : employeesToAdd){
            employee.setDepartment(department);
            employeeRepo.save(employee);
        }

        employeeRepo.flush();
        return employeesToAdd;
    }
}
