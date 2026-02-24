package com.example.HRMS.repos;

import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByAchievementPostOrderByCreatedAtDesc(AchievementPost post);

}
