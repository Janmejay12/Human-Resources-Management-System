package com.example.HRMS.services;

import com.example.HRMS.dtos.request.ChangeExpenseStatusRequest;
import com.example.HRMS.dtos.request.CreateExpenseRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.enums.ExpenseStatus;
import com.example.HRMS.mappers.ExpenseMapper;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.ExpenseRepository;
import com.example.HRMS.repos.TravelRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;
    private final TravelRepository travelRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, EmployeeRepository employeeRepository, TravelRepository travelRepository) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
        this.travelRepository = travelRepository;
    }

    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request, String email, Long travelId){
        Expense expense = ExpenseMapper.toEntity(request);

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );

        expense.setEmployee(employee);

        Travel travel =  travelRepository.findById(travelId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Travel not found with ID: " + travelId)
                );

        expense.setTravel(travel);

        expenseRepository.save(expense);
        ExpenseResponse response = ExpenseMapper.toDto(expense);


        return response;
    }

    public List<ExpenseResponse> getAllExpensesByTravelId(Long travelId) {
        List<Expense> expenses = expenseRepository.findAllByTravelId(travelId);

        List<Expense> filteredExpenses = expenses.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());


        List<ExpenseResponse> expenseResponseList = filteredExpenses.stream()
                .map(tr -> ExpenseMapper.toDto(tr))
                .collect(Collectors.toList());

        return expenseResponseList;
    }

    public List<ExpenseResponse> getMyExpenses(Long travelId, String email) {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );

        List<Expense> expenses = expenseRepository.findByTravelAndEmployee(travelId, employee.getEmployeeId());

        List<Expense> filteredExpenses = expenses.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<ExpenseResponse> expenseResponseList = filteredExpenses.stream()
                .map(tr -> ExpenseMapper.toDto(tr))
                .collect(Collectors.toList());

        return expenseResponseList;
    }

    public ExpenseResponse getExpenseById(Long travelId, Long expenseId) {
        Expense expense = expenseRepository.findByTravelAndExpenseId(travelId,expenseId).orElseThrow(() -> new EntityNotFoundException("Expense not found with expense ID: " + expenseId));
        if (expense.isDeleted())
            throw new EntityNotFoundException("Expense you are looking for is deleted");
        else
            return ExpenseMapper.toDto(expense);
    }
    @Transactional
    public ExpenseResponse changeExpenseStatus(ChangeExpenseStatusRequest request, String email,Long travelId, Long expenseId){

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));

        Expense expense = expenseRepository.findByTravelAndExpenseId(travelId,expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with expense ID: " + expenseId));

        Employee CreatorHR = expense.getTravel().getTravelCreatedBy();

        if(!CreatorHR.getEmployeeId().equals(employee.getEmployeeId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not authorized to update the status");
        }

        ExpenseStatus current = expense.getExpenseStatus();
        ExpenseStatus next = request.getStatus();

        if((current == ExpenseStatus.REJECTED) || (current == ExpenseStatus.APPROVED) && (current == ExpenseStatus.DRAFT)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Expense already finalised");
        }

        expense.setExpenseStatus(next);

        expenseRepository.save(expense);

        return getExpenseById(travelId, expense.getExpenseId());
    }
}
