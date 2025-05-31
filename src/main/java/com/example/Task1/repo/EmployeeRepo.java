package com.example.Task1.repo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Task1.entities.Employee;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    List<Employee> findByDepartment(String department);
    Optional<Employee> findByNameAndDepartment(String name, String department);
}




    
    
    

