package com.example.HRMS.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private Long commentedById;
    private String commentedBy;
    private String commentText;
    private Long postId;
}
