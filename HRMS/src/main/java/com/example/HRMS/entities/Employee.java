package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne()
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany()
    @JoinTable(
            name = "employee_travel",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "travel_id")
    )
    private List<Travel> tarvels = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name = "employee_travel_document",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "travel_document_id")
    )
    private List<TravelDocument> travelDocuments = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private EmployeeProfile employeeProfile;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Referal> referrals = new ArrayList<>();
}
