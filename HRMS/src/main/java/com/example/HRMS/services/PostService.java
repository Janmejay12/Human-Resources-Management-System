package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.CommentRequest;
import com.example.HRMS.dtos.request.PostRequest;
import com.example.HRMS.dtos.response.CommentResponse;
import com.example.HRMS.dtos.response.PostResponse;
import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Comment;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.enums.Roles;
import com.example.HRMS.mappers.CommentMapper;
import com.example.HRMS.mappers.PostMapper;
import com.example.HRMS.repos.AchievementPostRepository;
import com.example.HRMS.repos.CommentRepository;
import com.example.HRMS.repos.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final AchievementPostRepository achievementPostRepository;
    private final EmployeeRepository employeeRepository;
    private final Cloudinary cloudinary;
    private final CommentRepository commentRepository;

    public PostService(AchievementPostRepository achievementPostRepository,CommentRepository commentRepository, Cloudinary cloudinary, EmployeeRepository employeeRepository) {
        this.achievementPostRepository = achievementPostRepository;
        this.employeeRepository = employeeRepository;
        this.cloudinary = cloudinary;
        this.commentRepository = commentRepository;
    }

    public PostResponse createPost(PostRequest request, MultipartFile file, String email){
        Employee author = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        AchievementPost post = PostMapper.toEntity(request);
        post.setAuthor(author);

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()+"_"+post.getAchievementPostId());
        try {

            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            var doc = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/posts/"));

            post.setPostUrlPath(doc.get("url").toString());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload the file.");
        }

        AchievementPost savedPost = achievementPostRepository.save(post);

        return PostMapper.toDto(savedPost);
    }
    public void createSystemPost (PostRequest request){

        AchievementPost post = PostMapper.toEntity(request);
        post.setSystemPost(true);
        String urlString = "http://res.cloudinary.com/do1bqwwhv/image/upload/v1771323999/expenseDocuments/g1nnqryuy8a7oa8m9e8v.png";
        post.setPostUrlPath(urlString);
        System.out.println("post created");
        achievementPostRepository.save(post);
    }

    public CommentResponse createComment(CommentRequest request, Long postId, String email){
        Employee author = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        AchievementPost post = achievementPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement Post not found with ID: " + postId));

        Comment comment = CommentMapper.toEntity(request);

        comment.setCommentedBy(author);
        comment.setAchievementPost(post);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toDto(savedComment);

    }

    public List<PostResponse> getAllPosts() {

        List<AchievementPost> posts = achievementPostRepository.findAll();

        List<AchievementPost> filteredPosts = posts.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        return filteredPosts.stream()
                .map(PostMapper::toDto)
                .toList();
    }


    public List<PostResponse> getAllPostsByEmployeeId(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee not found with ID: " + employeeId));

        List<AchievementPost> posts =
                achievementPostRepository.findByAuthorOrderByCreatedAtDesc(employee);

        return posts.stream()
                .map(PostMapper::toDto)
                .toList();
    }

    public List<PostResponse> getAllMyPosts(String email) {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee not found with Email: " + email));

        List<AchievementPost> posts =
                achievementPostRepository.findByAuthorOrderByCreatedAtDesc(employee);

        List<AchievementPost> filteredPosts = posts.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        return filteredPosts.stream()
                .map(PostMapper::toDto)
                .toList();
    }


    public List<CommentResponse> getAllCommentsByPostId(Long postId) {

        AchievementPost post = achievementPostRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found with ID: " + postId));

        List<Comment> comments =
                commentRepository.findByAchievementPostOrderByCreatedAtDesc(post);

        return comments.stream()
                .map(CommentMapper::toDto)
                .toList();
    }
    public String likePost(String email, Long postId){
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee not found with Email: " + email));

        AchievementPost post = achievementPostRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found with ID: " + postId));

        post.setLikesCount(post.getLikesCount() + 1);
        achievementPostRepository.save(post);
        return "Like done";
    }

    public PostResponse getPostById(Long postId){
        AchievementPost post = achievementPostRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found with ID: " + postId));

        return PostMapper.toDto(post);
    }

    public String deletePost(Long postId, String email){
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee not found with Email: " + email));
        AchievementPost post = achievementPostRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found with ID: " + postId));

        if(employee.getRole().getRoleName() == Roles.HR){
            if(post.isDeleted()){
                throw new InvalidParameterException("Post is already deleted.");
            }else{
                post.setDeleted(true);
                achievementPostRepository.save(post);
                return "Post deleted successfully.";
            }
        }else{
            if(employee.getEmployeeId().equals(post.getAuthor().getEmployeeId())){
                if(post.isDeleted()){
                    throw new InvalidParameterException("Post is already deleted.");
                }else{
                    post.setDeleted(true);
                    achievementPostRepository.save(post);
                    return "Post deleted successfully.";
                }
            }
            else{
                throw new InvalidParameterException("Not authorized to delete this post");
            }
        }

    }

}
