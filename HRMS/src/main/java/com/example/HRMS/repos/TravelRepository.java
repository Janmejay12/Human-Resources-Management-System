package com.example.HRMS.repos;

import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Page<Travel> findDistinctByEmployeesEmployeeIdAndIsDeletedFalse(
            Long employeeId,
            Pageable pageable
    );

    Page<Travel> findByIsDeletedFalse(Pageable pageable);

}
