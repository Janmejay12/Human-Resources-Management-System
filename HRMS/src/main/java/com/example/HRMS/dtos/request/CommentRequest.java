package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotNull(message = "Comment text cannot be null")
    @Size(min = 5, message = "Comment text must be at least 5 characters long")
    private String commentText;
}
