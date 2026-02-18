package com.example.HRMS.entities;

import com.example.HRMS.enums.SlotBookingStatuses;
import com.example.HRMS.enums.SlotStatuses;
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

    @ManyToMany(mappedBy = "bookSlots")
    private List<Employee> players = new ArrayList<>();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "booked_by_id",nullable = false)
    private Employee bookedBy;

    @Column(name = "status")
    private SlotBookingStatuses status;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
