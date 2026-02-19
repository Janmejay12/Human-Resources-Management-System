package com.example.HRMS.services;

import com.example.HRMS.dtos.request.JobRequest;
import com.example.HRMS.dtos.request.ShareJobRequest;
import com.example.HRMS.dtos.request.UpdateJobRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.JobResponse;
import com.example.HRMS.dtos.response.ShareJobResponse;
import com.example.HRMS.entities.*;
import com.example.HRMS.enums.Roles;
import com.example.HRMS.mappers.ExpenseMapper;
import com.example.HRMS.mappers.JobMapper;
import com.example.HRMS.repos.DepartmentRepository;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final SmtpGmailSenderService smtpGmailSenderService;

    public JobService(JobRepository jobRepository,SmtpGmailSenderService smtpGmailSenderService, EmployeeRepository employeeRepository,DepartmentRepository departmentRepository) {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.smtpGmailSenderService = smtpGmailSenderService;

    }

    public List<JobResponse> getAllJobs(){
        List<Job> jobs = jobRepository.findAll();

        List<Job> filteredJobs = jobs.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<JobResponse> jobResponseList = filteredJobs.stream()
                .map(tr -> JobMapper.toDto(tr))
                .collect(Collectors.toList());

        return jobResponseList;
    }

    public JobResponse updateJob(UpdateJobRequest request, Long jobId, String email){
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new EntityNotFoundException("Job not found with ID: " + jobId)
        );
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );
        if(employee.getRole().getRoleName() != Roles.HR){
            throw new IllegalArgumentException("You are not authorized to create jobs");
        }

        if(request.getDefaultEmail() != null){
            job.setDefaultEmail(request.getDefaultEmail());
        }

        if(request.getOwnerId() != null){
            Employee jobOwner = employeeRepository.findById(request.getOwnerId()).orElseThrow(
                    () -> new EntityNotFoundException("Employee not found with ID: " + request.getOwnerId()));
            job.setHROwner(jobOwner);
            job.setDefaultEmail(jobOwner.getEmail());
        }

        if(request.getReviewerIds() != null){
            List<Long> reviewerIds = request.getReviewerIds();

            List<Employee> reviewers = reviewerIds.stream()
                    .map(id -> employeeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id))
                    ).toList();

            job.setCVReviewers(reviewers);

            for(Employee e : reviewers){
                e.getReviewedJobTitles().add(job);
            }
        }
        Job savedJob = jobRepository.saveAndFlush(job);
        return JobMapper.toDto(savedJob);

    }


    public JobResponse createJob(JobRequest request,String email){
        Job job = JobMapper.toEntity(request);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );
        if(employee.getRole().getRoleName() != Roles.HR){
            throw new IllegalArgumentException("You are not authorized to create jobs");
        }
        job.setCreatedBy(employee);
        job.setDefaultEmail(employee.getEmail());
        job.setHROwner(employee);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("department not found with ID: " + request.getDepartmentId())
        );
        job.setDepartment(department);

        if(!request.getReviewerIds().isEmpty()){
            List<Long> reviewerIds = request.getReviewerIds();

            List<Employee> reviewers = reviewerIds.stream()
                    .map(id -> employeeRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id))
                    ).toList();

            job.setCVReviewers(reviewers);
            for(Employee e : reviewers){
                e.getReviewedJobTitles().add(job);
            }
        }

        Job savedJob = jobRepository.save(job);
        return JobMapper.toDto(savedJob);
    }

    public ShareJobResponse shareJob(ShareJobRequest request, Long jobId, String email){
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new EntityNotFoundException("Job not found with ID: " + jobId)
        );

        for(String e : request.getRecipientEmails())
        {

            smtpGmailSenderService.sendEmail(
                    employee.getEmail()
                    ,e,
                    String.format("Career Opportunity: %s at Roima Intelligence",
                            job.getTitle()),
                    String.format(
                            "Dear Candidate,\n\n" +
                                    "I hope this email finds you well.\n\n" +
                                    "A career opportunity at Roima Intelligence is available that may align with your profile. " +
                                    "The organization is looking for a %s to join the team in %s.\n\n" +
                                    "Job Description is attached below.\n" +
                                    "If interested, please apply from the organization portal.\n\n" +
                                    "Best regards,\n" +
                                    "%s\n" +
                                    "Jr. Software Developer",
                             job.getTitle(), job.getLocation(), employee.getEmployeeName())
                    ,job.getJobDescriptionStorageUrl()
                    ,job.getTitle()+ " JD");

        }
        ShareJobResponse shareJobResponse = new ShareJobResponse();
        shareJobResponse.setMessage("Successfully sent emails to all Recipients");
        return shareJobResponse;
    }

}
