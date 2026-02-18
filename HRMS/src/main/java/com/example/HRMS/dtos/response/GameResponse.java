package com.example.HRMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    private Long gameId;
    private String gameName;
    private int slotMinutes;
    private LocalTime operatingStartHours;
    private LocalTime operatingEndHours;
}
