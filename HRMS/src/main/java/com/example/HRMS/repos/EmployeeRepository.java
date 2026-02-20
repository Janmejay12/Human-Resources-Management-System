package com.example.HRMS.repos;

import com.example.HRMS.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
     Optional<Employee> findByEmail(String email);
     boolean existsByEmail(String email);
     boolean existsByUserName(String username);

     @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId")
     List<Employee> findByManagerId(@Param("managerId") Long managerId);

     @Query(value =
             ";WITH ManagementChain AS (" +
                     "    SELECT * FROM employee WHERE employee_id = :empId " +
                     "    UNION ALL " +
                     "    SELECT e.* FROM employee e " +
                     "    INNER JOIN ManagementChain mc ON e.employee_id = mc.manager_id " +
                     ") " +
                     "SELECT * FROM ManagementChain",
             nativeQuery = true)
     List<Employee> findManagementChain(@Param("empId") Long empId);

}
