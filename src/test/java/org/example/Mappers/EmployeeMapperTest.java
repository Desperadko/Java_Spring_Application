package org.example.Mappers;

import org.example.DTOs.EmployeeDTO;
import org.example.Entities.Employee;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Test
    void convertDtoToEntity_Successful() {
        EmployeeDTO employeeDTO = new EmployeeDTO(
          "Tin4o",
          "Tin4ef",
          1001
        );

        Long employeeId = 1L;

        Employee employee = employeeMapper.convertDtoToEntity(employeeDTO, employeeId);

        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Tin4o");
        assertThat(employee.getLastName()).isEqualTo("Tin4ef");
        assertThat(employee.getEmployeeNumber()).isEqualTo(1001);
        assertThat(employee.getId()).isEqualTo(employeeId);
    }
}