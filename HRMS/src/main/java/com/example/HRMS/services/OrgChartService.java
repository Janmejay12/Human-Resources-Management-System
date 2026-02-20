package com.example.HRMS.services;

import com.example.HRMS.dtos.response.OrgChartNodeResponse;
import com.example.HRMS.dtos.response.OrgChartResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.repos.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgChartService {
    private final EmployeeRepository employeeRepository;

    public OrgChartService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public OrgChartResponse getOrgChart(Long empId){
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID :" + empId));
        OrgChartResponse response = new OrgChartResponse();

        List<Employee> managerialChainList = employeeRepository.findManagementChain(employee.getEmployeeId());

        List<OrgChartNodeResponse> managerialChainResponses = new ArrayList<>();
       for(Employee emp : managerialChainList) {
           OrgChartNodeResponse res = new OrgChartNodeResponse();
           res.setEmployeeId(emp.getEmployeeId());
           res.setDesignation(emp.getDesignation());
           res.setEmployeeName(emp.getEmployeeName());
           managerialChainResponses.add(res);
       }

        response.setManagerialChain(managerialChainResponses);

        List<Employee> directReports = employeeRepository.findByManagerId(employee.getEmployeeId());

        List<OrgChartNodeResponse> directReportsresponses = new ArrayList<>();

        for(Employee emp : managerialChainList) {
            OrgChartNodeResponse res = new OrgChartNodeResponse();
            res.setEmployeeId(emp.getEmployeeId());
            res.setDesignation(emp.getDesignation());
            res.setEmployeeName(emp.getEmployeeName());
            directReportsresponses.add(res);
        }

        response.setDirectReports(directReportsresponses);

       OrgChartNodeResponse orgChartNodeResponse = new OrgChartNodeResponse();
       orgChartNodeResponse.setEmployeeName(employee.getEmployeeName());
       orgChartNodeResponse.setDesignation(employee.getDesignation());
       orgChartNodeResponse.setEmployeeId(employee.getEmployeeId());
       response.setSelectedNode(orgChartNodeResponse);

        return response;
    }
}
