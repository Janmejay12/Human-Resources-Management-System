package com.example.HRMS.repos;

import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementPostRepository extends JpaRepository<AchievementPost, Long> {
    List<AchievementPost> findByAuthorOrderByCreatedAtDesc(Employee author);
}
