package com.example.HRMS.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeName;
    private Date birthDate;
    private Date joiningDate;
    private String email;
    private String password;
    private Department department;
    private Employee manager;

    private Role role;
    private List<Travel> tarvels;
    private List<TravelDocument> travelDocuments;
    private EmployeeProfile employeeProfile;
    private Referal referals;
}
