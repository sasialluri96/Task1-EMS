package com.example.Task1.test;
import com.example.Task1.DTO.EmployeeDTO;
import com.example.Task1.controller.EmployeeController;
import com.example.Task1.entities.Employee;
import com.example.Task1.exception.ResourceNotFoundException;
import com.example.Task1.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeService employeeService;

    @Test
    void testCreateEmployee_Success() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "HR");
        Employee employee = new Employee("John", "HR");
        employee.setId(1);
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(employee);
        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.department").value("HR"));
    }
    @Test
    void testCreateEmployee_NullName() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "HR");

        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateEmployee_Duplicate() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "HR");

        when(employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenThrow(new ResourceNotFoundException("Employee already exists"));

        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound()) // or whatever status you're returning for duplicates
                .andExpect(content().string(containsString("Employee already exists")));
    }



    @Test
    void testUpdateEmployee_Success() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("Jane", "Marketing");
        Employee employee = new Employee("Jane", "Marketing");
        employee.setId(1);
        when(employeeService.updateEmployee(anyInt(), any(EmployeeDTO.class))).thenReturn(employee);
        mockMvc.perform(put("/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.department").value("Marketing"));
    }
    @Test
    void testUpdateEmployee_RecordNotFound() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("Jane", "Marketing");
        when(employeeService.updateEmployee(anyInt(), any(EmployeeDTO.class))).thenThrow(new ResourceNotFoundException("Employee not found"));
        mockMvc.perform(put("/employees/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }
    @Test
    void testUpdateEmployee_NullName() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(null, "Marketing");
        mockMvc.perform(put("/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testDeleteEmployee_Success() throws Exception {
        mockMvc.perform(delete("/employees/delete/1"))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployee(1);
    }
    @Test
    void testDeleteEmployee_RecordNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Record not present")).when(employeeService).deleteEmployee(2);
        mockMvc.perform(delete("/employees/delete/2"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeleteEmployee_InvalidId() throws Exception {
        doThrow(new IllegalArgumentException("Invalid employee id")).when(employeeService).deleteEmployee(-1);
        mockMvc.perform(delete("/employees/delete/-1"))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testGetEmployeeById_Success() throws Exception {
        Employee employee = new Employee("John", "HR");
        employee.setId(1);
        when(employeeService.getEmployeeById(1)).thenReturn(employee);
        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.department").value("HR"));
    }
    @Test
    void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(2)).thenThrow(new ResourceNotFoundException("Record not present"));
        mockMvc.perform(get("/employees/2"))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetEmployeeById_InvalidId() throws Exception {
        when(employeeService.getEmployeeById(-1)).thenThrow(new IllegalArgumentException("Invalid employee id"));
        mockMvc.perform(get("/employees/-1"))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testGetEmployeesByDepartment_Success() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee("John", "HR");
        employee1.setId(1);
        Employee employee2 = new Employee("Jane", "HR");
        employee2.setId(2);
        employees.add(employee1);
        employees.add(employee2);
        when(employeeService.getEmployeesByDepartment("HR")).thenReturn(employees);
        mockMvc.perform(get("/employees/department/HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    void testGetEmployeesByDepartment_NotFound() throws Exception {
        when(employeeService.getEmployeesByDepartment("Finance")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/employees/department/Finance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void testGetEmployeesByDepartment_EmptyDepartment() throws Exception {
        mockMvc.perform(get("/employees/department/{department}", ""))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }




    @Test
    void testGetAllEmployees_Success() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee("John", "HR");
        employee1.setId(1);
        Employee employee2 = new Employee("Jane", "Marketing");
        employee2.setId(2);
        employees.add(employee1);
        employees.add(employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);
        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    void testGetAllEmployees_EmptyList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void testGetAllEmployees_Exception() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isInternalServerError());
    }
}