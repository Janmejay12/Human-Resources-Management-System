package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.CommentRequest;
import com.example.HRMS.dtos.request.PostRequest;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@AuthenticationPrincipal CustomEmployee user,
                                        @Valid @RequestPart("data") PostRequest request,
                                        @RequestPart("file") MultipartFile file
    ){
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // 2. Validate File Size (e.g., max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("File size exceeds 5MB");
        }

        // 3. Validate Content Type (e.g., only PDF, JPG, PNG)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/png"))) {
            return ResponseEntity.badRequest().body("Unsupported file type");
        }

        try{
            return ResponseEntity.ok(postService.createPost(request,file, user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomEmployee user,@Valid @RequestBody CommentRequest request, @PathVariable Long postId){
        try{
            return ResponseEntity.ok(postService.createComment(request, postId, user.getUsername() ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        try {
            return ResponseEntity.ok(postService.getAllPosts());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<?> getPostsByEmployeeId(
            @PathVariable Long employeeId) {

        try {
            return ResponseEntity.ok(
                    postService.getAllPostsByEmployeeId(employeeId));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getCommentsByPostId(
            @PathVariable Long postId) {

        try {
            return ResponseEntity.ok(
                    postService.getAllCommentsByPostId(postId));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@AuthenticationPrincipal CustomEmployee user) {

        try {
            return ResponseEntity.ok(
                    postService.getAllMyPosts(user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
