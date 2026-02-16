package com.example.HRMS.dtos.response;

import com.example.HRMS.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferalResponse {
    private Long referalId;
    private String candidateName;
    private String candidateEmail;
    private String shortNote;
    private String cvURLPath;
    private Long referalGivenBy;

}
