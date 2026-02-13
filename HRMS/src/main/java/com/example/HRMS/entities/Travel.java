package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
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

    private LocalDateTime startDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private LocalDateTime endDate;

    @ManyToMany(mappedBy = "travels")
    private List<Employee> employees =new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "travel_created_by_id")
    private Employee travelCreatedBy;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "max_per_day_allowance")
    private BigDecimal maxPerDayAllowance;


    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<TravelDocument> travelDocuments= new ArrayList<>();

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Expense> expenses= new ArrayList<>();

    @UpdateTimestamp
    private LocalDateTime statusChangedAt;

}
