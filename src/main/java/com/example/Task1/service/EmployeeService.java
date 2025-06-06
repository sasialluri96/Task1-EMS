package com.example.Task1.service;
import java.util.List;
import java.util.Optional;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Task1.DTO.EmployeeDTO;
import com.example.Task1.entities.Employee;
import com.example.Task1.exception.ResourceNotFoundException;
import com.example.Task1.repo.EmployeeRepo;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;
    public Employee saveEmployee(EmployeeDTO employeeDTO) {
        if (StringUtils.isBlank(employeeDTO.getName()) || StringUtils.isBlank(employeeDTO.getDepartment())) {
            throw new ResourceNotFoundException("Name and Department cannot be null or empty");
        }
        Optional<Employee> existing = employeeRepo.findByNameAndDepartment(
                employeeDTO.getName(), employeeDTO.getDepartment());
        if (existing.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists");
        }

        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setDepartment(employeeDTO.getDepartment());
        return employeeRepo.save(employee);
    }

    public Employee updateEmployee(int id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Record not present"));
    
        employee.setName(employeeDTO.getName());
        employee.setDepartment(employeeDTO.getDepartment());
    
        return employeeRepo.save(employee);
    }
    public String deleteEmployee(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Record not present");
        }
        employeeRepo.deleteById(id);
        return null;
    }
    public Employee getEmployeeById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not present"));
    }
//    public List<Employee> getEmployeesByDepartment(String department) {
//        List<Employee> employees = employeeRepo.findByDepartment(department);
//        if (employees.isEmpty()) {
//            throw new ResourceNotFoundException("User not found");
//        }
//        return employees;
//    }
    public List<Employee> getEmployeesByDepartment(String department) {
        if (department == null || department.isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
        return employeeRepo.findByDepartment(department);
    }
    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
}
