package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.ChangeTravelStatusRequest;
import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.request.UpdateTravelRequest;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.TravelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/travels")
public class TravelController {
    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    //@PreAuthorize("hasRole('HR')")
    @PostMapping()
    public ResponseEntity<?> createTravel(@AuthenticationPrincipal CustomEmployee user, @Valid @RequestBody TravelCreateRequest request){
        try{
            TravelResponse travelResponse = travelService.createTravel(request,user.getUsername());
            return ResponseEntity.ok(travelResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

       // @PreAuthorize("hasRole('HR')")
        @GetMapping()
    public ResponseEntity<?> getAllTravels(){
            try{
                return ResponseEntity.ok(travelService.getAllTravels());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/{id}")
    public ResponseEntity<TravelResponse> getTravelById(@PathVariable long id){
        try{
            return ResponseEntity.ok(travelService.getTravelById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> changeTravelStatus(@AuthenticationPrincipal CustomEmployee user
                                ,@RequestBody ChangeTravelStatusRequest request
                                ,@PathVariable Long id)
    {
        try{
            return ResponseEntity.ok(travelService.changeTravelStatus(request, user.getUsername(), id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/my-travels")
    public ResponseEntity<?> getMyTravels(@AuthenticationPrincipal CustomEmployee user){
        try{
            return ResponseEntity.ok(travelService.gettravelsForEmployee(user.getUsername()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{travelId}")
    public ResponseEntity<?> updateTravel(@Valid @RequestBody UpdateTravelRequest request, @PathVariable Long travelId){
        try{
            TravelResponse travelResponse = travelService.updateTravel(request,travelId);
            return ResponseEntity.ok(travelResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable Long id){
        try{
            return ResponseEntity.ok(travelService.deleteTravel(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

