package org.example.Repositories;

import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setup(){
        Department devDepartment = new Department();
        devDepartment.setName("Game Dev");
        departmentRepository.saveAndFlush(devDepartment);

        Employee employee1 = new Employee();
        employee1.setFirstName("Tin4o");
        employee1.setLastName("Tin4ef");
        employee1.setEmployeeNumber(1001);
        employee1.setDepartment(devDepartment);

        Employee employee2 = new Employee();
        employee2.setFirstName("Vik4o");
        employee2.setLastName("Vik4ef");
        employee2.setEmployeeNumber(1002);
        employee2.setDepartment(devDepartment);

        Employee employee3 = new Employee();
        employee3.setFirstName("John");
        employee3.setLastName("SpringBoot");
        employee3.setEmployeeNumber(1003);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.flush();
    }

    @Test
    void findEmployeeByEmployeeNumber_Successful() {
        var employee = employeeRepository.findEmployeeByEmployeeNumber(1001);

        assertThat(employee.get().getFirstName())
                .isNotNull()
                .isEqualTo("Tin4o");
    }

    @Test
    void findEmployeesByDepartment_Successful() {
        var department = departmentRepository.findDepartmentByName("Game Dev");
        var employeesOptional = employeeRepository.findEmployeesByDepartment(department.get());
        var employees = employeesOptional.get();

        assertThat(employees.get(0).getFirstName())
                .isNotNull()
                .isEqualTo("Tin4o");
        assertThat(employees.get(1).getFirstName())
                .isNotNull()
                .isEqualTo("Vik4o");
        assertThat(employees.stream().count())
                .isEqualTo(2);
    }

    @Test
    void removeEmployeeFromDepartment_Successful() {
        employeeRepository.deleteById(3L);
        var employees = employeeRepository.findAll();

        assertThat(employees.get(employees.size() - 1).getFirstName())
                .isEqualTo("Vik4o");
        assertThat(employees.stream().count())
                .isEqualTo(2L);
    }
}