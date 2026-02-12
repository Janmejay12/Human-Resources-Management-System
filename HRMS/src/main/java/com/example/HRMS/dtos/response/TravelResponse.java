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
    private Long travelId;
    private String travelTitle;
    private String location;
    private String purpose;
    private Date startDate;
    private Long travelCreatedBy;
    private Date endDate;
    private List<Long> travellers;
    private Statuses status;
}
