package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.ReferalRequest;
import com.example.HRMS.dtos.response.ReferalResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Job;
import com.example.HRMS.entities.Referal;
import com.example.HRMS.mappers.ReferalMapper;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.JobRepository;
import com.example.HRMS.repos.ReferalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ReferalService {
    private final ReferalRepository referalRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private  final Cloudinary cloudinary;

    public ReferalService(ReferalRepository referalRepository, EmployeeRepository employeeRepository, JobRepository jobRepository, Cloudinary cloudinary) {
        this.referalRepository = referalRepository;
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.cloudinary = cloudinary;
    }

    public ReferalResponse createReferal(ReferalRequest request, String email, MultipartFile file, Long jobId){
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );
        Referal referal = ReferalMapper.toEntity(request);
        referal.setReferalGivenBy(employee);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Job not found with ID: " + jobId)
                );

        referal.setJob(job);

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()+"_"+referal.getReferalId());
        try {

            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            var doc = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/JDs/"));

            referal.setCvURLPath(doc.get("url").toString());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload the file.");
        }

        Referal savedReferal = referalRepository.save(referal);
        ReferalResponse response = ReferalMapper.toDto(savedReferal);
        return  response;
    }
}
