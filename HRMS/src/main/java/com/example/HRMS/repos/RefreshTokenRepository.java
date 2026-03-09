package com.example.HRMS.repos;

import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(Employee user);

    void deleteByUser(Employee user);
}
