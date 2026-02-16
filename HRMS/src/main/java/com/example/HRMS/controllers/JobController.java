package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.JobRequest;
import com.example.HRMS.dtos.request.UpdateJobRequest;
import com.example.HRMS.dtos.response.JobResponse;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping()
    public ResponseEntity<?> createJob(@AuthenticationPrincipal CustomEmployee user,@Valid @RequestBody JobRequest request){
        try{
            return ResponseEntity.ok(jobService.createJob(request, user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());}
    }

    @GetMapping()
    public ResponseEntity<List<JobResponse>> getAllJobs(){
        try{
            return ResponseEntity.ok(jobService.getAllJobs());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@AuthenticationPrincipal CustomEmployee user,@Valid @RequestBody UpdateJobRequest request,
                                       @PathVariable Long id){
        try{
            return ResponseEntity.ok(jobService.updateJob(request,id,user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());}
    }


}
