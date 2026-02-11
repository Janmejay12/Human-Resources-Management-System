package com.example.HRMS.services;

import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.mappers.ExpenseMappper;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.ExpenseRepository;
import com.example.HRMS.repos.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;
    private final TravelRepository travelRepository;
    private final ExpenseMappper expenseMappper;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository,ExpenseMappper expenseMappper, EmployeeRepository employeeRepository, TravelRepository travelRepository) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
        this.travelRepository = travelRepository;
        this.expenseMappper = expenseMappper;
    }

    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request){
        Expense expense = expenseMappper.toEntity(request);

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with ID: " + request.getEmployeeId())
                );

        expense.setEmployee(employee);

        Travel travel =  travelRepository.findById(request.getTravelId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Travel not found with ID: " + request.getTravelId())
                );

        expense.setTravel(travel);
        expenseRepository.save(expense);
        ExpenseResponse response = expenseMappper.toDto(expense);
        return response;
    }

    public List<ExpenseResponse> getAllExpensesByTravelId(Long travelId) {
        List<Expense> expenses = expenseRepository.findAllByTravelId(travelId);

//        List<Travel> filteredTravels = travels.stream()
//                .filter(tr -> !tr.isDeleted())
//                .collect(Collectors.toList());

        List<ExpenseResponse> expenseResponseList = expenses.stream()
                .map(tr -> expenseMappper.toDto(tr))
                .collect(Collectors.toList());

        return expenseResponseList;
    }

    public List<ExpenseResponse> getAllExpensesByEmployeeId(Long employeeId) {
        List<Expense> expenses = expenseRepository.findAllByEmployeeId(employeeId);

//        List<Travel> filteredTravels = travels.stream()
//                .filter(tr -> !tr.isDeleted())
//                .collect(Collectors.toList());

        List<ExpenseResponse> expenseResponseList = expenses.stream()
                .map(tr -> expenseMappper.toDto(tr))
                .collect(Collectors.toList());

        return expenseResponseList;
    }

    public ExpenseResponse getExpenseById(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + expenseId));
        if (expense.isDeleted())
            throw new EntityNotFoundException("Expense you are looking for is deleted");
        else
            return expenseMappper.toDto(expense);
    }
}
