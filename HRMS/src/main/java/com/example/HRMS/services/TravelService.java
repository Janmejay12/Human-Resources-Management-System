package com.example.HRMS.services;

import com.example.HRMS.dtos.request.ChangeTravelStatusRequest;
import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.request.UpdateTravelRequest;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Status;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.enums.Statuses;
import com.example.HRMS.mappers.TravelMapper;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.StatusRepository;
import com.example.HRMS.repos.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelService {
    private final TravelRepository travelRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusRepository statusRepository;
    private final SmtpGmailSenderService smtpGmailSenderService;

    public TravelService(TravelRepository travelRepository,SmtpGmailSenderService smtpGmailSenderService, EmployeeRepository employeeRepository, StatusRepository statusRepository) {
        this.travelRepository = travelRepository;
        this.employeeRepository = employeeRepository;
        this.statusRepository = statusRepository;
        this.smtpGmailSenderService = smtpGmailSenderService;
    }
    @Transactional
    public TravelResponse createTravel(TravelCreateRequest request, String email) {

        Travel travel = TravelMapper.toEntity(request);

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        travel.setTravelCreatedBy(employee);

        Status status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + request.getStatusId()));
        travel.setStatus(status);

      List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());

        employees = employees.stream()
                .filter(e -> !e.isDeleted())
                .toList();

        List<Employee> conflictedEmployees = employees.stream()
                .filter(emp -> emp.getTravels().stream()
                        .anyMatch(tr ->
                                // Check if existing travel starts before new ends
                                // AND existing travel ends after new starts
                                tr.getStartDate().isBefore(request.getEndDate())

                                        &&

                                        tr.getEndDate().isAfter(request.getStartDate())
                        )
                )
                .collect(Collectors.toList());

        if(!conflictedEmployees.isEmpty())
            throw new EntityNotFoundException("Travel date conflict with selected employees.");

       if(employees.size() != request.getEmployeeIds().size()){
          throw new EntityNotFoundException("Invalid employees list");      }

      travel.setEmployees(employees);


      for(Employee e : employees){
          e.getTravels().add(travel);
      }

      Travel savedTravel = travelRepository.saveAndFlush(travel);

        for(Employee e : savedTravel.getEmployees()) {
            smtpGmailSenderService.sendEmail(savedTravel.getTravelCreatedBy().getEmail(), e.getEmail(), String.format("Action Required: Travel Booking Confirmation for %s to %s", savedTravel.getTravelTitle(), savedTravel.getLocation()),

                    String.format(
                            "Dear Team Member,\n\n" +
                                    "You have been selected for a business trip as part of the following travel plan.\n\n" +
                                    "Trip Details:\n" +
                                    "Plan Name: %s\n" +
                                    "Destination: %s\n" +
                                    "Departure: %s\n" +
                                    "Return: %s\n\n" +
                                    "Please review the itinerary and contact HR for any further queries.\n\n" +
                                    "Best regards,\n" +
                                    "HR Department",
                            savedTravel.getTravelTitle(),
                            savedTravel.getLocation(),
                            savedTravel.getStartDate(),
                            savedTravel.getEndDate()
                    )
            );
        }

      TravelResponse travelResponse = TravelMapper.toDto(savedTravel);

        return travelResponse;
    }

    public List<TravelResponse> getAllTravels() {
        List<Travel> travels = travelRepository.findAll();

        List<Travel> filteredTravels = travels.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<TravelResponse> travelResponseList = filteredTravels.stream()
                .map(tr -> TravelMapper.toDto(tr))
                .collect(Collectors.toList());

        return travelResponseList;
    }

    public TravelResponse getTravelById(Long travelId) {
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new EntityNotFoundException("Travel not found with ID: " + travelId));
        if (travel.isDeleted())
            throw new EntityNotFoundException("Travel you are looking for is deleted");
        else
            return TravelMapper.toDto(travel);
    }

    @Transactional
    public TravelResponse changeTravelStatus(ChangeTravelStatusRequest request, String email, Long travelId){

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found with ID: " + travelId));

//        boolean validEmployee = travel.getEmployees().stream()
//                .anyMatch(tr -> tr.getEmployeeId().equals(employee.getEmployeeId()));

        boolean isCreator = travel.getTravelCreatedBy().getEmployeeId().equals(employee.getEmployeeId());

//        boolean isTraveller = travel.getEmployees().stream()
//                .anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId()));


        if(!isCreator)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only creator of the travel");

        Statuses currentStatus = travel.getStatus().getStatusName();

        if((currentStatus == Statuses.Cancelled) || currentStatus == Statuses.Completed){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status is already finalized");
        }
        Status status = statusRepository.findByStatusName(request.getStatus());

        travel.setStatus(status);
        travelRepository.save(travel);

        return getTravelById(travel.getTravelId());
    }

    public List<TravelResponse> gettravelsForEmployee(String email){
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        List<Travel> travels = travelRepository.findTravelsByEmployeeId(employee.getEmployeeId());

        return travels.stream().map(TravelMapper :: toDto).toList();
    }
    @Transactional
    public TravelResponse updateTravel(UpdateTravelRequest request, Long travelId){
        Travel existingTravel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found with ID: " + travelId));

        existingTravel = TravelMapper.toUpdatedEntity(request);

        List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());

        employees = employees.stream()
                .filter(e -> !e.isDeleted())
                .toList();

        List<Employee> conflictedEmployees = employees.stream()
                .filter(emp -> emp.getTravels().stream()
                        .anyMatch(tr ->
                                // Check if existing travel starts before new ends
                                // AND existing travel ends after new starts
                                tr.getStartDate().isBefore(request.getEndDate())

                                        &&

                                        tr.getEndDate().isAfter(request.getStartDate())
                        )
                )
                .collect(Collectors.toList());

        if(!conflictedEmployees.isEmpty())
            throw new EntityNotFoundException("Travel date conflict with selected employees.");

        if(employees.size() != request.getEmployeeIds().size()){
            throw new EntityNotFoundException("Invalid employees list");      }

        existingTravel.setEmployees(employees);


        for(Employee e : employees){
            e.getTravels().add(existingTravel);
        }// i want to check if the old travelers are removed or not.

        Travel savedTravel = travelRepository.saveAndFlush(existingTravel);

        TravelResponse travelResponse = TravelMapper.toDto(savedTravel);
        return travelResponse;
    }

    public TravelResponse deleteTravel(Long travelId){
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found with ID: " + travelId));

        if(travel.isDeleted()){
            throw new IllegalArgumentException("Travel is already deleted");
        }

        travel.setDeleted(true);
        return getTravelById(travelId);
    }
}