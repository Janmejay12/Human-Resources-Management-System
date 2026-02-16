package com.example.HRMS.entities;

import com.example.HRMS.enums.Statuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status_name", nullable = false)
    private Statuses statusName;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private List<Travel> travels = new ArrayList<>();

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private List<BookSlot> bookSlots= new ArrayList<>();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

}
