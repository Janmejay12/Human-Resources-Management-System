package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.request.JobRequest;
import com.example.HRMS.dtos.request.ReferalRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.JobResponse;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Job;
import com.example.HRMS.entities.Referal;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public static Job toEntity(JobRequest request) {
        if (request == null) {
            return null;
        }

        Job job = new Job();
        job.setLocation(request.getLocation());
        job.setTitle(request.getTitle());
        job.setJobDescriptionStorageUrl(request.getJobDescriptionStorageUrl());
        job.setEmploymentType(request.getEmploymentType());
        job.setPostedDate(request.getPostedDate());
        job.setSalaryRange(request.getSalaryRange());
        job.setSkillList(request.getSkillList());
        job.setStatus(request.getStatus());
        return job;
    }

    public static JobResponse toDto(Job job){
        if (job == null) {
            return null;
        }

        JobResponse jobResponse = new JobResponse();
        jobResponse.setCreatedBy(job.getCreatedBy().getEmployeeId());
        jobResponse.setLocation(job.getLocation());
        jobResponse.setHROwner(job.getHROwner().getEmployeeId());
        jobResponse.setEmploymentType(job.getEmploymentType());
        jobResponse.setTitle(job.getTitle());
        jobResponse.setStatus(job.getStatus());
        jobResponse.setJobDescriptionStorageUrl(job.getJobDescriptionStorageUrl());
        jobResponse.setSkillList(job.getSkillList());
        jobResponse.setPostedDate(job.getPostedDate());
        jobResponse.setJobId(job.getJobId());
        jobResponse.setDepartmentId(job.getDepartment().getDepartmentId());
        jobResponse.setDefaultEmail(job.getDefaultEmail());
        jobResponse.setSalaryRange(job.getSalaryRange());
        return jobResponse;
    }
}
