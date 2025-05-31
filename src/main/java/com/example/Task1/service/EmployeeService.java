package com.example.Task1.service;
import java.util.List;
import java.util.Optional;

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
    //    public Employee saveEmployee(EmployeeDTO employeeDTO){
//        Employee employee=new Employee();
//        employee.setName(employeeDTO.getName());
//        employee.setDepartment(employeeDTO.getDepartment());
//        return employeeRepo.save(employee);
//    }
    public Employee saveEmployee(EmployeeDTO employeeDTO){
        Optional<Employee> existing = employeeRepo.findByNameAndDepartment(
                employeeDTO.getName(), employeeDTO.getDepartment());
        if (existing.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with same name and department");
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
    public void deleteEmployee(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Record not present");
        }
        employeeRepo.deleteById(id);
    }
    public Employee getEmployeeById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not present"));
    }
    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employees = employeeRepo.findByDepartment(department);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return employees;
    }
    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
}
