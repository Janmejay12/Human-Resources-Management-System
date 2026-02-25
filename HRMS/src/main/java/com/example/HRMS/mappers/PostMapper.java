package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.JobRequest;
import com.example.HRMS.dtos.request.PostRequest;
import com.example.HRMS.dtos.response.CommentResponse;
import com.example.HRMS.dtos.response.JobResponse;
import com.example.HRMS.dtos.response.PostResponse;
import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Job;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public static AchievementPost toEntity(PostRequest request) {
        if (request == null) {
            return null;
        }
        AchievementPost post = new AchievementPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setLikesCount(0);
        return post;
    }

    public static PostResponse toDto(AchievementPost achievementPost) {
        if (achievementPost == null) {
            return null;
        }
        PostResponse response = new PostResponse();
        response.setPostId(achievementPost.getAchievementPostId());
        response.setContent(achievementPost.getContent());
        response.setLikesCount(achievementPost.getLikesCount());
        response.setTitle(achievementPost.getTitle());
        response.setSystemPost(achievementPost.isSystemPost());
        if(!achievementPost.isSystemPost()){
            response.setAuthorName(achievementPost.getAuthor().getEmployeeName());
            response.setAuthorId(achievementPost.getAuthor().getEmployeeId());
        }
        response.setPostUrlPath(achievementPost.getPostUrlPath());
        response.setComments(achievementPost.getComments()
                .stream()
                .map((comment) -> {
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setPostId(comment.getAchievementPost().getAchievementPostId());
                    commentResponse.setCommentText(comment.getCommentText());
                    commentResponse.setCommentedById(comment.getCommentedBy().getEmployeeId());
                    commentResponse.setCommentId(comment.getCommentId());
                    return commentResponse;
        })
                .toList());
        return response;
    }
}
