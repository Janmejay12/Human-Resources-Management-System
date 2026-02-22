package com.example.HRMS.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSlotRequest {
    private Long gameSlotId;

    private List<Long> playerIds;

}
