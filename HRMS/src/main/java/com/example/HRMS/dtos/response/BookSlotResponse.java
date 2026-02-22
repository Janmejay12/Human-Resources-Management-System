package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.SlotBookingStatuses;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSlotResponse {
    private Long bookingId;

    private Long gameSlotId;

    private LocalDate slotDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long bookedById;

    private List<Long> playerIds;

    private SlotBookingStatuses status;
}
