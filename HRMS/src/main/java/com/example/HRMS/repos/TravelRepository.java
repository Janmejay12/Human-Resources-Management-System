package com.example.HRMS.repos;

import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("""
SELECT DISTINCT t FROM Travel t 
JOIN t.employees e 
WHERE e.employeeId = :employeeId AND t.isDeleted = false
""")
List<Travel> findTravelsByEmployeeId(Long employeeId);
}
