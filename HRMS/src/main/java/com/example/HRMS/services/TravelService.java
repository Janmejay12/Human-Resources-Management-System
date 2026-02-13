package com.example.HRMS.services;

import com.example.HRMS.dtos.request.ChangeTravelStatusRequest;
import com.example.HRMS.dtos.request.TravelCreateRequest;
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
    private final ModelMapper modelMapper;

    public TravelService(TravelRepository travelRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository, StatusRepository statusRepository) {
        this.travelRepository = travelRepository;
        this.employeeRepository = employeeRepository;
        this.statusRepository = statusRepository;
        this.modelMapper = modelMapper;
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

      employeeRepository.saveAllAndFlush(employees);

      TravelResponse travelResponse = TravelMapper.toDto(travel);

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
            return modelMapper.map(travel, TravelResponse.class);
    }

    @Transactional
    public TravelResponse changeTravelStatus(ChangeTravelStatusRequest request, String email, Long travelId){

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel not found with ID: " + travelId));

        boolean validEmployee = travel.getEmployees().stream()
                .anyMatch(tr -> tr.getEmployeeId().equals(employee.getEmployeeId()));

        boolean isCreator = travel.getTravelCreatedBy().getEmployeeId().equals(employee.getEmployeeId());

        boolean isTraveller = travel.getEmployees().stream()
                .anyMatch(e -> e.getEmployeeId().equals(employee.getEmployeeId()));


        if(!isCreator && !isTraveller)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only craetor of the travel or traveller can update the travel status.");

        Statuses currentStatus = travel.getStatus().getStatusName();

        if((currentStatus == Statuses.Cancelled) || currentStatus == Statuses.Completed){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status is already finalized");
        }
        Status status = statusRepository.findByStatusName(request.getStatus());

        travel.setStatus(status);
        travelRepository.save(travel);

        return getTravelById(travel.getTravelId());
    }
}