package com.example.HRMS.repos;

import com.example.HRMS.entities.Referal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferalRepository extends JpaRepository<Referal, Long> {
}
