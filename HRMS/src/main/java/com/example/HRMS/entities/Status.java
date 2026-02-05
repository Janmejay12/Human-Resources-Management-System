package com.example.HRMS.entities;

import com.example.HRMS.enums.Statuses;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;
    private Statuses statusName;

    private List<Travel> travels;
    private List<BookSlot> bookSlots;
    private List<Job> jobs;
}
