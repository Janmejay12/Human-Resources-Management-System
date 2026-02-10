package com.example.HRMS.services;

import com.example.HRMS.dtos.request.RegisterRequest;
import com.example.HRMS.dtos.response.EmployeeResponse;
import com.example.HRMS.dtos.response.RegisterResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.repos.DepartmentRepository;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public AdminService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public RegisterResponse registerEmployee(RegisterRequest request){
        if(employeeRepository.existsByEmail(request.getEmail()) )
        {
            throw new IllegalArgumentException("Email address already in use.");
        }
        if(employeeRepository.existsByUserName(request.getUserName()) )
        {
            throw new IllegalArgumentException("username  already in use.");
        }

        Employee employee = modelMapper.map(request,Employee.class);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        employee.setPassword(encodedPassword);

        if(request.getDepartmentId() != null){
            employee.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + request.getDepartmentId())));
        }

        if(request.getManagerId() != null){
            employee.setManager(employeeRepository.findById(request.getManagerId()).orElseThrow(() -> new EntityNotFoundException("Manager not found with ID: " + request.getManagerId())));
        }

        if(request.getRoleId() != null){
            employee.setRole(roleRepository.findById(request.getRoleId()).orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + request.getRoleId())));
        }
    RegisterResponse response = modelMapper.map(employeeRepository.save(employee),RegisterResponse.class);
            return response;
    }


    public List<EmployeeResponse> getAllEmployees(){
       List<Employee> employees = employeeRepository.findAll();

       List<Employee> filteredEmployees = employees.stream()
               .filter(emp -> !emp.isDeleted())
               .collect(Collectors.toList());

       List<EmployeeResponse> employeeDTOs = filteredEmployees.stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponse.class))
                .collect(Collectors.toList());
       return employeeDTOs;
    }
}