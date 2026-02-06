package com.example.HRMS.repos;

import com.example.HRMS.entities.AchievementPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementPostRepository extends JpaRepository<AchievementPost, Long> {
}
