package com.example.HRMS.repos;

import com.example.HRMS.entities.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Page<Travel> findDistinctByEmployeesEmployeeIdAndIsDeletedFalse(
            Long employeeId,
            Pageable pageable
    );

    Page<Travel> findByIsDeletedFalse(Pageable pageable);

}
