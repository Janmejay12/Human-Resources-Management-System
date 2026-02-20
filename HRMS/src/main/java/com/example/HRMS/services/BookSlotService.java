package com.example.HRMS.services;

import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotBookingStatuses;
import com.example.HRMS.repos.BookSlotRepository;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.GameSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSlotService {
    private final BookSlotRepository bookSlotRepository;
    private final GameSlotRepository gameSlotRepository;
    private final EmployeeRepository employeeRepository;

    public BookSlotService(BookSlotRepository bookSlotRepository, GameSlotRepository gameSlotRepository, EmployeeRepository employeeRepository) {
        this.bookSlotRepository = bookSlotRepository;
        this.gameSlotRepository = gameSlotRepository;
        this.employeeRepository = employeeRepository;
    }

//    @Transactional
//    public BookSlot bookSlot(Long slotId, Long bookedById, List<Long> playerIds){
//        if(playerIds.size() != 2 && playerIds.size() != 4){
//            throw new IllegalArgumentException("Only 2 or 4 players allowed in a slot.");
//        }
//
//        GameSlot gameSlot = gameSlotRepository.findByIdForUpdate(slotId).orElseThrow(
//                () -> new EntityNotFoundException("Slot not found with ID: " + slotId)
//        );
//        Employee bookedBy = employeeRepository.findById(bookedById).orElseThrow(
//                () -> new EntityNotFoundException("Slot not found with ID: " + bookedById)
//        );
//        List<Employee> players = employeeRepository.findAllById(playerIds);
//
//        boolean isSlotBooked = bookSlotRepository.existsByGameSlotAndStatus(gameSlot, SlotBookingStatuses.BOOKED);
//
//        validateNoConsecutiveBooking(gameSlot, bookedBy);
//
//        boolean hasPlayedToday = bookSlotRepository.hasPLayedToday(bookedById,gameSlot.getSlotDate());
//
//    }
}
