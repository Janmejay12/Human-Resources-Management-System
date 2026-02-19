package com.example.HRMS.dtos.response;

import com.example.HRMS.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrgChartResponse {
    private List<OrgChartNodeResponse> managerialChain = new ArrayList<>();
    private List<OrgChartNodeResponse> directReports = new ArrayList<>();
    private OrgChartNodeResponse selectedNode;
}
