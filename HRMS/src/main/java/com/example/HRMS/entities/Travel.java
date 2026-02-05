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
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelId;
    private String location;
    private String purpose;
    private Date startDate;
    private Date endDate;

    private Status status;
    private List<TravelDocument> travelDocuments;
    private List<Expense> expenses;

}
