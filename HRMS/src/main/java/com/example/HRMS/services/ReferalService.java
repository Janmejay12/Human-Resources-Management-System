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
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReferalService {
    private final ReferalRepository referalRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private  final Cloudinary cloudinary;
    private final SmtpGmailSenderService smtpGmailSenderService;

    public ReferalService(ReferalRepository referalRepository, SmtpGmailSenderService smtpGmailSenderService,EmployeeRepository employeeRepository, JobRepository jobRepository, Cloudinary cloudinary) {
        this.referalRepository = referalRepository;
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.cloudinary = cloudinary;
        this.smtpGmailSenderService = smtpGmailSenderService;
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

        //send email to HROwner
        smtpGmailSenderService.sendEmail(savedReferal.getReferalGivenBy().getEmail(), job.getDefaultEmail(),"Job Referral - " + job.getTitle() + " - " + savedReferal.getCandidateName(),
                "Hi "+job.getCreatedBy().getEmployeeName()+", \n" +
                        "I would like to refer "+savedReferal.getCandidateName()+" for the "+job.getTitle()+" "+job.getJobId()+" position. \n" +
                        "\n" +
                        "\n" +
                        "Based on my knowledge of their skills, I believe "+savedReferal.getCandidateName()+" would be a fantastic addition to the team. They possess strong skills in software Development and DBMS and are very enthusiastic about this opportunity. \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Candidate Details:\n" +
                        "Name: "+savedReferal.getCandidateName()+"\n" +
                        "Email: "+savedReferal.getCandidateEmail()+"\n" +
                        "Please let me know if you need any further information.\n" +
                        "Best regards,\n" +
                        savedReferal.getReferalGivenBy().getEmployeeName()+" "+ savedReferal.getReferalGivenBy().getEmployeeId()+"\n" +
                        savedReferal.getReferalGivenBy().getDepartment().getDepartmentName()
                        ,savedReferal.getCvURLPath(),savedReferal.getCandidateName()+ " Resume");

        //send email to CV reviewers
        for(Employee e : job.getCVReviewers())
        {

            smtpGmailSenderService.sendEmail(
                    savedReferal.getReferalGivenBy().getEmail()
                    ,e.getEmail(),
                    String.format("Action Required: CV Review Invitation - %s (%s)",job.getTitle(), job.getJobId()),
                    String.format("Dear Reviewer,\n\n" +
                            "You have been selected to review candidate CVs for the following position:\n\n" +
                            "Job Title: %s\n" +
                            "Job ID: %s\n" +
                            "Department: %s\n" +
                            "Candidate Name: %s\n\n" +
                            "Best regards,\n" +
                            "HR Recruitment Team",
                    job.getTitle(),
                    job.getJobId(),
                    job.getDepartment(),
                    savedReferal.getCandidateName())
                    ,savedReferal.getCvURLPath()
                    ,savedReferal.getCandidateName()+ " Resume");

        }



        ReferalResponse response = ReferalMapper.toDto(savedReferal);
        return  response;
    }
}
