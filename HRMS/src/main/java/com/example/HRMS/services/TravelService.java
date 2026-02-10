package com.example.HRMS.services;

import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.response.EmployeeResponse;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.StatusRepository;
import com.example.HRMS.repos.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public TravelResponse createTravel(TravelCreateRequest request) {
        System.out.println("1");
        Travel travel = modelMapper.map(request, Travel.class);
        System.out.println("2");
        travel.setStatus(statusRepository.findById(request.getStatusId()).orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + request.getStatusId())));
        System.out.println("3");
        travel.setEmployees(request.getEmployeeIds()
                .stream()
                .map(id -> employeeRepository.findById(id))
                .filter(Optional::isPresent) // Remove empty results
                .map(Optional::get)
                .filter(emp -> !emp.isDeleted())// Unwrap
                .collect(Collectors.toList()));
        System.out.println("4");
        return modelMapper.map(travelRepository.save(travel), TravelResponse.class);
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