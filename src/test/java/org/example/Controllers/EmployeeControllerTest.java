package org.example.Controllers;

import jakarta.transaction.Transactional;
import org.example.DTOs.EmployeeDTO;
import org.example.Services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    void setup(){
        employeeService.addEmployee(
          new EmployeeDTO(
                  "Tin4o",
                  "Tin4ef",
                  1001
          )
        );
        employeeService.addEmployee(
          new EmployeeDTO(
                  "Vik4o",
                  "Vik4ef",
                  1002
          )
        );
        employeeService.addEmployee(
          new EmployeeDTO(
                  "John",
                  "SpringBoot",
                  1003
          )
        );
    }

    @Test
    void getEmployeeByID_Success() throws Exception {
        mockMvc.perform(get("/employee/{employeeId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Tin4o"));
    }
    @Test
    void getEmployees_Success() throws Exception {
        mockMvc.perform(get("/employee?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstName").value("Tin4o"))
                .andExpect(jsonPath("$.content[1].firstName").value("Vik4o"));
    }
}