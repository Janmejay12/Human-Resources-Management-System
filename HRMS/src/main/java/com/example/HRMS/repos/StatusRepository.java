package com.example.HRMS.repos;

import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("SELECT s FROM Status s WHERE s.statusName = :name")
    Status findByStatusName(@Param("name") String name);
}
