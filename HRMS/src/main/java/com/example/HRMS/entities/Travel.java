package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelId;

    private String travelTitle;

    @Column(nullable = false)
    private String location;

    private String purpose;

    private Date startDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private Date endDate;

    @ManyToMany(mappedBy = "travels")
    private List<Employee> employees =new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "travel_created_by_id")
    private Employee travelCreatedBy;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<TravelDocument> travelDocuments= new ArrayList<>();

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Expense> expenses= new ArrayList<>();

}
