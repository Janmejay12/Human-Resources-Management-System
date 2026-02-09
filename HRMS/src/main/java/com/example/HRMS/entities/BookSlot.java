package com.example.HRMS.entities;

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

    @ManyToMany(mappedBy = "bookSlots")
    private List<Employee> players = new ArrayList<>();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private String bookedBy;

    @ManyToOne()
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
