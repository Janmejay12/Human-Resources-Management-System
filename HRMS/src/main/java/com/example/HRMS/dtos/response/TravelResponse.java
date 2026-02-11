package com.example.HRMS.dtos.response;

import com.example.HRMS.enums.Statuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelResponse {
    private String travelTitle;
    private String location;
    private String purpose;
    private Date startDate;
    private Date endDate;
    private List<String> travellers;
    private Statuses status;
}
