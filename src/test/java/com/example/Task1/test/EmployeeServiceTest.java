package com.example.Task1.test;
import com.example.Task1.DTO.EmployeeDTO;
import com.example.Task1.entities.Employee;
import com.example.Task1.exception.ResourceNotFoundException;
import com.example.Task1.repo.EmployeeRepo;
import com.example.Task1.service.EmployeeService;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepo employeeRepo;
    private Employee sampleEmployee;
    private EmployeeDTO validDto;
    @BeforeEach
    void setup() {
        sampleEmployee = new Employee("John", "HR");
        sampleEmployee.setId(1);
        validDto = new EmployeeDTO();
        validDto.setName("John");
        validDto.setDepartment("HR");

    }


    @Test
    void testSaveEmployee_Success() {
        when(employeeRepo.save(any(Employee.class))).thenReturn(sampleEmployee);
        Employee saved = employeeService.saveEmployee(validDto);
        assertNotNull(saved);
        assertEquals("John", saved.getName());
        verify(employeeRepo, times(1)).save(any(Employee.class));
    }
    @Test
    void testSaveEmployee_NullName() {
        validDto.setName(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(validDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    void testSaveEmployee_Duplicate() {
        when(employeeRepo.findByNameAndDepartment("John", "HR"))
                .thenReturn(Optional.of(sampleEmployee));
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(validDto));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Employee already exists", ex.getMessage());
    }




    @Test
    void testUpdateEmployee_Success() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepo.save(any(Employee.class))).thenReturn(sampleEmployee);
        Employee updated = employeeService.updateEmployee(1, validDto);
        assertNotNull(updated);
        assertEquals("John", updated.getName());
    }
    @Test
    void testUpdateEmployee_RecordNotFound() {
        when(employeeRepo.findById(2)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(2, validDto));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Record not present", ex.getMessage());
    }
    @Test
    void testUpdateEmployee_NullName() {
        validDto.setName(null);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(validDto);
        assertFalse(violations.isEmpty());
    }



    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepo.existsById(1)).thenReturn(true);
        doNothing().when(employeeRepo).deleteById(1);
        employeeService.deleteEmployee(1);
        verify(employeeRepo, times(1)).deleteById(1);
    }
    @Test
    void testDeleteEmployee_RecordNotFound() {
        when(employeeRepo.existsById(2)).thenReturn(false);
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(2));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Record not present", ex.getMessage());
    }
    @Test
    void testDeleteEmployee_InvalidId() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> employeeService.deleteEmployee(-1));
    }



    @Test
    void testGetEmployeeById_Success() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(sampleEmployee));
        Employee emp = employeeService.getEmployeeById(1);
        assertNotNull(emp);
        assertEquals("John", emp.getName());
    }
    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepo.findById(2)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(2));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Record not present", ex.getMessage());
    }
    @Test
    void testGetEmployeeById_InvalidId() {
        when(employeeRepo.findById(-1)).thenThrow(new IllegalArgumentException("Invalid employee id"));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.getEmployeeById(-1));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Invalid employee id", ex.getMessage());
    }




    @Test
    void testGetEmployeesByDepartment_Success() {
        when(employeeRepo.findByDepartment("HR")).thenReturn(List.of(sampleEmployee));
        List<Employee> employees = employeeService.getEmployeesByDepartment("HR");
        assertFalse(employees.isEmpty());
        assertEquals("HR", employees.get(0).getDepartment());
    }
    @Test
    void testGetEmployeesByDepartment_NotFound() {
        when(employeeRepo.findByDepartment("Finance")).thenReturn(Collections.emptyList());
        List<Employee> employees = employeeService.getEmployeesByDepartment("Finance");
        assertTrue(employees.isEmpty());
    }
    @Test
    void testGetEmployeesByDepartment_EmptyDepartment() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> employeeService.getEmployeesByDepartment(""));
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Department cannot be empty", ex.getMessage());
    }



    @Test
    void testGetAllEmployees_Success() {
        when(employeeRepo.findAll()).thenReturn(List.of(sampleEmployee));
        List<Employee> employees = employeeService.getAllEmployees();
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getName());
    }

    @Test
    void testGetAllEmployees_EmptyList() {
        when(employeeRepo.findAll()).thenReturn(Collections.emptyList());
        List<Employee> employees = employeeService.getAllEmployees();
        assertTrue(employees.isEmpty());
    }

    @Test
    void testGetAllEmployees_Exception() {
        when(employeeRepo.findAll()).thenThrow(new RuntimeException("Database error"));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> employeeService.getAllEmployees());
        System.out.println("Exception Message: " + ex.getMessage());
        assertEquals("Database error", ex.getMessage());
    }
}