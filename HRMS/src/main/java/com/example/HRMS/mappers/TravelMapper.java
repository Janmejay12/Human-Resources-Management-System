package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.request.UpdateTravelRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.Travel;
import org.springframework.stereotype.Component;

@Component
public class TravelMapper {
    public static Travel toEntity(TravelCreateRequest request){
        if (request == null) {
            return null;
        }
        Travel travel = new Travel();
        travel.setTravelTitle(request.getTravelTitle());
        travel.setEndDate(request.getEndDate());
        travel.setLocation(request.getLocation());
        travel.setStartDate(request.getStartDate());
        travel.setMaxPerDayAllowance(request.getMaxPerDayAllowance());
        travel.setPurpose(request.getPurpose());
        return travel;
    }
    public static Travel toUpdatedEntity(UpdateTravelRequest request){
        if (request == null) {
            return null;
        }
        Travel travel = new Travel();
        travel.setTravelTitle(request.getTravelTitle());
        travel.setEndDate(request.getEndDate());
        travel.setLocation(request.getLocation());
        travel.setStartDate(request.getStartDate());
        travel.setMaxPerDayAllowance(request.getMaxPerDayAllowance());
        travel.setPurpose(request.getPurpose());
        return travel;
    }

    public static TravelResponse toDto(Travel travel){
        if (travel == null) {
            return null;
        }
        TravelResponse travelResponse = new TravelResponse();
        travelResponse.setStatus(travel.getStatus().getStatusName());
        travelResponse.setTravelTitle(travel.getTravelTitle());
        travelResponse.setTravelId(travel.getTravelId());
        travelResponse.setPurpose(travel.getPurpose());
        travelResponse.setMaxPerDayAllowance(travel.getMaxPerDayAllowance());
        travelResponse.setEndDate(travel.getEndDate());
        travelResponse.setStartDate(travel.getStartDate());
        travelResponse.setTravellers(travel.getEmployees()
                .stream()
                .map(Employee::getEmployeeId)
                .toList());
        travelResponse.setLocation(travel.getLocation());
        travelResponse.setTravelCreatedBy(travel.getTravelCreatedBy().getEmployeeId());
        return travelResponse;
    }
}
