package org.example.Repositories;

import org.example.Entities.Department;
import org.example.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//JpaRepository<type na twa det 6te wzima i slaga w bazata danni, type na id-to mu>
//handlewa wkarwaneto i izkarwaneto na entities ot bazata danni - CRUD operaciite
//get all users, delete user by id, update use, etc..
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //praznite metodi ba4kat, 6toto JpaRepository izpolzwa naming convention za da im izwur6i logikata
    //demek findEmployeeByEmployeeNum
    //si go basically razbira i prevru6ta w sql raven na
    //select e from Employee e where e.employeeNum = :num
    boolean existsEmployeeByEmployeeNumber(Integer num);
    Optional<Employee> findEmployeeByEmployeeNumber(Integer num);
    Optional<List<Employee>> findEmployeesByDepartment(Department department);
    Optional<List<Employee>> findAllByIdIn(List<Long> Ids);
    @Modifying
    @Query(value = "UPDATE Employee e SET e.department = :department WHERE e.id = :employeeId")
    void updateEmployeeDepartment(Long employeeId, Department department);
    @Modifying
    @Query(value = "UPDATE employees e SET e.department_id = :departmentId WHERE e.id = :employeeId", nativeQuery = true)
    void updateEmployeeDepartmentNative(Long employeeId, Long departmentId);
    @Modifying
    @Query(value = "UPDATE Employee e SET e.department = null WHERE e.id = :employeeId")
    void removeEmployeeFromDepartment(Long employeeId);
    @Modifying
    @Query(value = "UPDATE Employee e SET e.department = null WHERE e.id = :employeeId AND e.department.id = :departmentId")
    int removeEmployeeFromDepartmentIfExists(Long employeeId, Long departmentId);
}
