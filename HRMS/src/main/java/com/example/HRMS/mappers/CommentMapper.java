package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.CommentRequest;
import com.example.HRMS.dtos.response.CommentResponse;
import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Comment;
import com.example.HRMS.entities.Employee;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public static Comment toEntity(
            CommentRequest request
    ) {
        Comment comment = new Comment();

        comment.setCommentText(request.getCommentText());

        return comment;
    }


    // Convert Comment Entity → CommentResponse DTO
    public static CommentResponse toDto(Comment comment) {

        CommentResponse response = new CommentResponse();

        response.setCommentId(comment.getCommentId());
        response.setCommentedBy(comment.getCommentedBy().getEmployeeName());
        response.setPostId(comment.getAchievementPost().getAchievementPostId());
        response.setCommentText(comment.getCommentText());
        response.setCommentedById(comment.getCommentedBy().getEmployeeId());

        return response;
    }

}
