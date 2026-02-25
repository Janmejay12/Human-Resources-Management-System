package com.example.HRMS.schedulers;

import com.example.HRMS.dtos.request.PostRequest;
import com.example.HRMS.entities.AchievementPost;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.repos.AchievementPostRepository;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.services.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostScheduler {
    private final EmployeeRepository employeeRepository;
    private final PostService postService;

    @Scheduled(cron = "0 0 6 * * ?")
//    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void systemPost()
        {
        log.info("Running post scheduler");
        LocalDate today = LocalDate.now();
        List<Employee> birthdayEmployees = employeeRepository.findEmployeesWithBirthdayToday(today);
        if(!birthdayEmployees.isEmpty()){

            for(Employee emp : birthdayEmployees ){
                PostRequest request = new PostRequest();
                request.setTitle("Happy Birthday to " + emp.getEmployeeName());
                request.setContent("Dear "+emp.getEmployeeName()+" the team of Roima Intelligence wishes you a very happy birthday and we wish you great success in our journey together.");
                postService.createSystemPost(request);
            }
        }

        List<Employee> joiningEmployees = employeeRepository.findEmployeesWithJoiningDateToday(today);
        if(!joiningEmployees.isEmpty()){
            for(Employee emp : joiningEmployees)
            {
                        PostRequest request = new PostRequest();

                        LocalDate joiningDate = emp.getJoiningDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate currentDate = LocalDate.now();
                        long yearsWorked = ChronoUnit.YEARS.between(joiningDate, currentDate);

                        request.setTitle("Happy Work Anniversary to " + emp.getEmployeeName());
                        request.setContent(emp.getEmployeeName()+" completed "+yearsWorked+" with Roima Intelligence today and we are glad to have you onboard.");
                        postService.createSystemPost(request);
            }

        }
    }
}
