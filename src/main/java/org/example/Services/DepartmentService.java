package org.example.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.DTOs.DepartmentDTO;
import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.example.Mappers.DepartmentMapper;
import org.example.Repositories.DepartmentRepository;
import org.example.Repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //department specific funkcii
    public Page<Department> findAllDepartments(Pageable pageable){
        return departmentRepo.findAll(pageable);
    }
    public Department findDepartmentById(Long departmentId){
        return getDepartment(departmentId);
    }
    public Department addDepartment(DepartmentDTO departmentDTO){
        //prowerka dali department s dadenoto ime su6testwuwa
        Optional<Department> dbObject = departmentRepo.findDepartmentByName(departmentDTO.name());
        if(dbObject.isPresent()) {
            throw new IllegalStateException("Department with name " + departmentDTO.name() + " is already present." +
                    "Choose another name..");
        }

        //pri zadadena null stoinost za departmentId awtomati4no mu se zadawa ot tablicata
        //tui kato sme go nastroili da e unique primary key, koito se auto incrementira
        return saveDepartmentDtoToDatabase(departmentDTO, null);
    }
    public Department updateDepartmentMetadata(Long departmentId, DepartmentDTO departmentDTO){
        if(!departmentRepo.existsById(departmentId))
            throw new EntityNotFoundException("Department ID: " + departmentId);
        return saveDepartmentDtoToDatabase(departmentDTO, departmentId);
    }
    public void deleteDepartment(Long departmentId){
        var department = getDepartment(departmentId);
        var employees = getEmployeesFromDepartment(department);

        employeeRepo.removeEmployeesFromTheirDepartment(employees);

        departmentRepo.delete(department);
    }

    //vklu4wa6ti i employees
    public Page<Employee> getEmployeesFromDepartment(Long departmentId, Pageable pageable){
        var department = getDepartment(departmentId);

        return getEmployeesFromDepartment(department, pageable);
    }
    public void addEmployeeToDepartment(Long departmentId, Long employeeId) {
        var department = getDepartment(departmentId);

        employeeRepo.updateEmployeeDepartment(employeeId, department);
    }
    public void addEmployeeToDepartmentNative(Long departmentId, Long employeeId){
        employeeRepo.updateEmployeeDepartmentNative(employeeId, departmentId);
    }
    public Employee removeEmployeeFromDepartment(Long departmentId, Long employeeId) {
        var employee = getEmployee(employeeId);

        int updatedRows = employeeRepo.removeEmployeeFromDepartmentIfExists(employeeId, departmentId);
        if (updatedRows == 0)
            return null;

        return employeeRepo.saveAndFlush(employee);
    }
    public List<Employee> updateEmployeeListForDepartment(Long departmentId, List<Long> employeeIds) {
        var department = getDepartment(departmentId);
        var currentEmployeesInDepartment = getEmployeesFromDepartment(department);

        employeeRepo.removeEmployeesFromTheirDepartment(currentEmployeesInDepartment);

        var employeesToAdd = getEmployeesFromListOfIds(employeeIds);

        employeeRepo.updateEmployeesDepartments(employeesToAdd, department);

        return employeesToAdd;
    }

    //pomo6ni funkcii
    private Department saveDepartmentDtoToDatabase(DepartmentDTO departmentDTO, Long departmentId) {
        var department = departmentMapper.convertDtoToEntity(departmentDTO, departmentId);

        return departmentRepo.saveAndFlush(department);
    }
    private List<Employee> getEmployeesFromDepartment(Department department) {
        return employeeRepo.findEmployeesByDepartment(department)
                .orElseThrow(() -> new EntityNotFoundException("Employees not found with department ID: " + department.getId()));
    }
    private Page<Employee> getEmployeesFromDepartment(Department department, Pageable pageable){
        return employeeRepo.findEmployeesByDepartment(department, pageable);
    }
    private Department getDepartment(Long departmentId) {
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department ID: " + departmentId));
    }
    private Employee getEmployee(Long employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee ID: " + employeeId));
    }
    private List<Employee> getEmployeesFromListOfIds(List<Long> employeeIds) {
        return employeeRepo.findAllByIdIn(employeeIds)
                .orElseThrow(() -> {
                    String employeeListToStr = Arrays.stream(employeeIds.toArray())
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "));
                    return new EntityNotFoundException("Couldn't find employees with the given IDs: " + employeeListToStr);
                });
    }
}
