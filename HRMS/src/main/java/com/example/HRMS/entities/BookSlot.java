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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookSlotId;
    private Date startTime;
    private Date endTime;

    private Game game;

    private List<Employee> players;

    private Employee bookedBy;

    private Statuses status;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

}
