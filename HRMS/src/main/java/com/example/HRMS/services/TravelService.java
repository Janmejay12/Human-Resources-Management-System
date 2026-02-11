package com.example.HRMS.services;

import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Status;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.StatusRepository;
import com.example.HRMS.repos.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

        Travel travel = new Travel();

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        travel.setTravelTitle(request.getTravelTitle());
        travel.setEndDate(request.getEndDate());
        travel.setLocation(request.getLocation());
        travel.setStartDate(request.getStartDate());
        travel.setPurpose(request.getPurpose());
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
                                tr.getStartDate().before(request.getEndDate())

                                        &&

                                        tr.getEndDate().after(request.getStartDate())
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

      TravelResponse travelResponse = new TravelResponse();
      travelResponse.setStatus(status.getStatusName());
      travelResponse.setTravelTitle(travel.getTravelTitle());
      travelResponse.setPurpose(travel.getPurpose());
      travelResponse.setEndDate(travel.getEndDate());
      travelResponse.setStartDate(travel.getStartDate());
      travelResponse.setLocation(travel.getLocation());
      travelResponse.setTravelCreatedBy(employee.getEmployeeName());



      List<String> employeeNames = employees.stream()
                .map(Employee::getEmployeeName)
                        .toList();

      travelResponse.setTravellers(employeeNames);
      return travelResponse;
    }

    public List<TravelResponse> getAllTravels() {
        List<Travel> travels = travelRepository.findAll();

        List<Travel> filteredTravels = travels.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<TravelResponse> travelResponseList = filteredTravels.stream()
                .map(tr -> modelMapper.map(tr, TravelResponse.class))
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
}