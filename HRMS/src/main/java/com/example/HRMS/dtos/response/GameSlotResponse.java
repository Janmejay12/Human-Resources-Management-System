package com.example.HRMS.dtos.response;

import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.Game;
import com.example.HRMS.enums.SlotStatuses;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSlotResponse {
    private Long gameSlotId;

    private Long slotNumber;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate slotDate;


    private Long gameId;

    private SlotStatuses status;
}
