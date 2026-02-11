package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.TravelCreateRequest;
import com.example.HRMS.dtos.response.RegisterResponse;
import com.example.HRMS.dtos.response.TravelResponse;
import com.example.HRMS.services.TravelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/travels")
public class TravelController {
    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping()
    public ResponseEntity<?> createTravel( @Valid @RequestBody TravelCreateRequest request){
        try{
            TravelResponse travelResponse = travelService.createTravel(request);
            return ResponseEntity.ok(travelResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

       // @PreAuthorize("hasRole('HR')")
        @GetMapping()
    public ResponseEntity<List<TravelResponse>> getAllTravels(){
            try{
                return ResponseEntity.ok(travelService.getAllTravels());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
        }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/{id}")
    public ResponseEntity<TravelResponse> getTravelById(@PathVariable long id){
        try{
            return ResponseEntity.ok(travelService.getTravelById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }
    }

