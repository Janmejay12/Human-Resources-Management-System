package com.example.HRMS.entities;

import com.example.HRMS.enums.SlotStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameSlotId;

    @OneToMany(mappedBy = "gameSlot")
    private List<BookSlot> bookSlots = new ArrayList<>();

    @Column(name = "slot_number")
    private Long slotNumber;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_date")
    private LocalDate slotDate;

    @ManyToOne()
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "status")
    private SlotStatuses status;
}
