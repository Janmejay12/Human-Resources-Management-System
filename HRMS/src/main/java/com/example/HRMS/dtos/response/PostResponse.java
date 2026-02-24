package com.example.HRMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long postId;
    private String authorName;
    private int likesCount;
    private String title;
    private String content;
    private Long authorId;
    private String postUrlPath;
    private List<CommentResponse> comments;
}
