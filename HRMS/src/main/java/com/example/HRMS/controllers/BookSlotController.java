package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.BookSlotRequest;
import com.example.HRMS.dtos.response.BookSlotResponse;
import com.example.HRMS.securityClasses.CustomEmployee;
import com.example.HRMS.services.BookSlotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookSlotController {
    private final BookSlotService bookSlotService;

    public BookSlotController(BookSlotService bookSlotService) {
        this.bookSlotService = bookSlotService;
    }

    @PostMapping("/book-slots")
    public ResponseEntity<?> bookGameSlot(@AuthenticationPrincipal CustomEmployee user,@Valid  @RequestBody BookSlotRequest request){
        try{
            return ResponseEntity.ok(bookSlotService.bookSlot(request.getGameSlotId(), user.getUsername(), request.getPlayerIds()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(
            @PathVariable Long bookingId
    )
    {
        try{
            bookSlotService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/my-bookings/today")
    public ResponseEntity<List<BookSlotResponse>>
    getTodayBookings(@AuthenticationPrincipal CustomEmployee user) {

        return ResponseEntity.ok(
                bookSlotService.getTodayBookings(user.getUsername())
        );
    }
}
