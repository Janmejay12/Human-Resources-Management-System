package com.example.HRMS.repos;

import com.example.HRMS.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
     Optional<Employee> findByEmail(String email);
     boolean existsByEmail(String email);
     boolean existsByUserName(String username);

}
