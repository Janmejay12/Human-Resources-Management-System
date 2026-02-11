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
    public TravelResponse createTravel(TravelCreateRequest request) {

        Travel travel = new Travel();
        travel.setTravelTitle(request.getTravelTitle());
        travel.setEndDate(request.getEndDate());
        travel.setLocation(request.getLocation());
        travel.setStartDate(request.getStartDate());
        travel.setPurpose(request.getPurpose());

        Status status = statusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + request.getStatusId()));
        travel.setStatus(status);

      List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());

        employees = employees.stream()
                .filter(e -> !e.isDeleted())
                .toList();

//        List<Employee> conflictedEmployees = employees.stream()
//                .filter(emp -> emp.getTravels().stream()
//                        .anyMatch(tr ->
//                                // Check if existing travel starts before new ends
//                                // AND existing travel ends after new starts
//                                tr.getStartDate().isBefore((request.getEndDate()) &&
//                                        tr.getEndDate().isAfter(request.getStartDate())
//                        )
//                )
//                .collect(Collectors.toList()));



      if(employees.size() != request.getEmployeeIds().size()){
          throw new EntityNotFoundException("Invalid employees list");      }

      travel.setEmployees(employees);

      for(Employee employee : employees){
          employee.getTravels().add(travel);
      }

        Travel saved = travelRepository.save(travel);

        TravelResponse travelResponse = modelMapper.map(saved, TravelResponse.class);
        travelResponse.setStatus(status.getStatusName());

        System.out.println(travelResponse.getStartDate() + " " + travelResponse.getEndDate());

        List<String> employeeNames = employees.stream()
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());

        for(String e : employeeNames){
            System.out.println(e);
        }


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