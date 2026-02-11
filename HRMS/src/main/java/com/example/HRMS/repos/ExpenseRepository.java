package com.example.HRMS.repos;

import com.example.HRMS.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    @Query("SELECT e FROM Expense e WHERE e.travel.id = :travelId")
    List<Expense> findAllByTravelId(@Param("travelId") Long travelId);

    @Query("SELECT e FROM Expense e WHERE e.employee.id = :employeeId")
    List<Expense> findAllByEmployeeId(@Param("employeeId") Long employeeId);
}
