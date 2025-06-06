package com.example.Task1.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Task1.DTO.EmployeeDTO;
import com.example.Task1.entities.Employee;
import com.example.Task1.service.EmployeeService;

import jakarta.validation.Valid;
    @RestController
    @RequestMapping("/employees")
    public class EmployeeController {
        @Autowired
        private EmployeeService employeeService;

        @PostMapping("/add")
        public ResponseEntity<Employee> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
            return new ResponseEntity<>(employeeService.saveEmployee(employeeDTO), HttpStatus.CREATED);
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDTO) {
            return new ResponseEntity<>(employeeService.updateEmployee(id, employeeDTO), HttpStatus.OK);
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
            employeeService.deleteEmployee(id);
            return new ResponseEntity<>("Employee deleted successfully!", HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Employee> getById(@PathVariable int id) {
            return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
        }

        @GetMapping("/department/{department}")
        public ResponseEntity <List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
            List<Employee> employees = employeeService.getEmployeesByDepartment(department);
            return ResponseEntity.ok(employees);
        }
        @GetMapping("/all")
        public List<Employee> getAllEmployees() {
            return employeeService.getAllEmployees();
        }
    }