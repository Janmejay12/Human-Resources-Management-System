package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.SlotStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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
