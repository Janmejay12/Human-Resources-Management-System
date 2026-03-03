package com.example.HRMS.services;

import com.example.HRMS.dtos.response.BookSlotResponse;
import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotBookingStatuses;
import com.example.HRMS.enums.SlotStatuses;
import com.example.HRMS.repos.BookSlotRepository;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.GameSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Transactional
    public BookSlotResponse bookSlot(Long slotId, String email, List<Long> playerIds) {

        if (playerIds.size() != 2 && playerIds.size() != 4) {
            throw new IllegalArgumentException("Only 2 or 4 players allowed in a slot.");
        }

        GameSlot gameSlot = gameSlotRepository.findByIdForUpdate(slotId).orElseThrow(
                () -> new EntityNotFoundException("Slot not found with ID: " + slotId)
        );
        Employee bookedBy = employeeRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Slot not found with Email: " + email)
        );

        List<Employee> players = employeeRepository.findAllById(playerIds);

        validateNoConsecutiveBooking(gameSlot, bookedBy);

        boolean isSlotBooked = bookSlotRepository.existsByGameSlotAndStatusAndIsDeletedFalse(gameSlot, SlotBookingStatuses.BOOKED);


        boolean hasPlayedToday = bookSlotRepository.hasPlayedToday(bookedBy.getEmployeeId(), gameSlot.getSlotDate(), SlotBookingStatuses.BOOKED);

        BookSlot booking = new BookSlot();

        booking.setGameSlot(gameSlot);
        booking.setBookedBy(bookedBy);
        booking.setPlayers(players);
        booking.setDeleted(false);

        if (isSlotBooked) {
            booking.setStatus(SlotBookingStatuses.WAITING);
        } else {
            if (hasPlayedToday) {
                booking.setStatus(SlotBookingStatuses.WAITING);
            } else {
                booking.setStatus((SlotBookingStatuses.BOOKED));
                gameSlot.setStatus(SlotStatuses.BOOKED);
                gameSlotRepository.save(gameSlot);
            }
        }
        BookSlot savedSlot = bookSlotRepository.save(booking);

        BookSlotResponse response = new BookSlotResponse();
        response.setBookedById(savedSlot.getBookedBy().getEmployeeId());
        response.setBookingId(savedSlot.getBookSlotId());
        response.setSlotDate(savedSlot.getGameSlot().getSlotDate());
        response.setStartTime(savedSlot.getGameSlot().getStartTime());
        response.setEndTime(savedSlot.getGameSlot().getEndTime());
        response.setGameSlotId(savedSlot.getGameSlot().getGameSlotId());
        response.setStatus(savedSlot.getStatus());
        response.setPlayerIds(
                savedSlot.getPlayers()
                        .stream()
                        .map(employee -> employee.getEmployeeId())
                        .toList()
        );

        return response;

    }

    @Transactional
    public void cancelBooking(Long bookingid) {
        BookSlot booking = bookSlotRepository.findByIdForUpdate(bookingid)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus((SlotBookingStatuses.CANCELLED));
        booking.setDeleted(true);
        promoteNextIfAvailable(booking.getGameSlot());
    }

    @Transactional
    public void promoteNextIfAvailable(GameSlot gameSlot) {
        boolean alreadyConfirmed = bookSlotRepository.existsByGameSlotAndStatusAndIsDeletedFalse(gameSlot, SlotBookingStatuses.BOOKED);

        if (alreadyConfirmed) return;

        List<BookSlot> queue = bookSlotRepository.findByGameSlotAndStatusAndIsDeletedFalseOrderByCreatedAtAsc(gameSlot, SlotBookingStatuses.WAITING);

        if (queue.isEmpty()) {
            gameSlot.setStatus(SlotStatuses.EMPTY);
            gameSlotRepository.save(gameSlot);
            return;
        }

        LocalDate slotData = gameSlot.getSlotDate();

        queue.sort(
                Comparator.comparing((BookSlot b) -> bookSlotRepository.hasPlayedToday(
                        b.getBookedBy().getEmployeeId(),
                        slotData,
                        SlotBookingStatuses.BOOKED)
                ).thenComparing(BookSlot::getCreatedAt)
        );

        BookSlot next = queue.get(0);

        next.setStatus(SlotBookingStatuses.BOOKED);

        gameSlot.setStatus(SlotStatuses.BOOKED);
        gameSlotRepository.save(gameSlot);

        bookSlotRepository.save(next);
    }


    private void validateNoConsecutiveBooking(GameSlot slot, Employee employee) {
        List<GameSlot> previousSlots =
                gameSlotRepository.findAllBySlotDateAndSlotNumber(
                        slot.getSlotDate(),
                        slot.getSlotNumber() - 1
                );

        if (previousSlots.isEmpty()) return;

        GameSlot previousSlot = previousSlots.get(0);

        boolean exists =
                bookSlotRepository.existsByGameSlotAndBookedByAndStatusAndIsDeletedFalse(
                        previousSlot,
                        employee,
                        SlotBookingStatuses.BOOKED
                );

        if (exists) {
            throw new RuntimeException("Cannot book consecutive slot");
        }
    }

    @Transactional
    public List<BookSlotResponse> getTodayBookings(String email) {

        LocalDate today = LocalDate.now();

        Employee booker = employeeRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Slot not found with Email: " + email)
        );
        List<BookSlot> bookings =
                bookSlotRepository.findTodayBookings(booker.getEmployeeId(), today);

        List<BookSlotResponse> responseList = new ArrayList<>();

        for (BookSlot booking : bookings) {

            GameSlot slot = booking.getGameSlot();

            List<Long> playerIds =
                    booking.getPlayers()
                            .stream()
                            .map(Employee::getEmployeeId)
                            .toList();

            BookSlotResponse response =
                    BookSlotResponse.builder()
                            .bookingId(booking.getBookSlotId())
                            .gameSlotId(slot.getGameSlotId())
                            .slotDate(slot.getSlotDate())
                            .startTime(slot.getStartTime())
                            .endTime(slot.getEndTime())
                            .status(booking.getStatus())
                            .bookedById(booking.getBookedBy().getEmployeeId())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }
}
















