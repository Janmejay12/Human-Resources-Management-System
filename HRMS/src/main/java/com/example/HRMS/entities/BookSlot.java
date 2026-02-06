package com.example.HRMS.entities;

import com.example.HRMS.enums.Statuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
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

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @ManyToOne()
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToMany()
    @JoinTable(
            name = "employee_book_slot",
            joinColumns = @JoinColumn(name = "book_slot_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> players = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "booked_by_id", nullable = false)
    private Employee bookedBy;

    @ManyToOne()
    @JoinColumn(name = "status_id", nullable = false)
    private Statuses status;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
