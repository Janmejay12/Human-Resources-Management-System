package com.example.HRMS.repos;

import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotBookingStatuses;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookSlotRepository extends JpaRepository<BookSlot,Long> {
    boolean existsByGameSlotAndStatusAndIsDeletedFalse(
            GameSlot slot,
            SlotBookingStatuses status
    );

    boolean existsByGameSlotAndBookedByAndStatusAndIsDeletedFalse(
            GameSlot slot,
            Employee employee,
            SlotBookingStatuses status
    );

    @Query("""
SELECT COUNT(b) > 0
FROM BookSlot b
WHERE b.bookedBy.employeeId = :employeeId
AND b.gameSlot.slotDate = :date
AND b.status = :status
AND b.isDeleted = false
""")
    boolean hasPlayedToday(
            Long employeeId,
            LocalDate date,
            SlotBookingStatuses status
    );

    List<BookSlot> findByGameSlotAndStatusAndIsDeletedFalseOrderByCreatedAtAsc(
            GameSlot slot,
            SlotBookingStatuses status
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BookSlot b WHERE b.bookSlotId = :id")
    Optional<BookSlot> findByIdForUpdate(Long id);

    List<BookSlot> findByGameSlotAndIsDeletedFalse(GameSlot slot);

    @Query("""
SELECT b
FROM BookSlot b
JOIN FETCH b.gameSlot gs
LEFT JOIN FETCH b.players
WHERE b.bookedBy.employeeId = :employeeId
AND gs.slotDate = :today
AND b.isDeleted = false
ORDER BY gs.startTime ASC
""")
    List<BookSlot> findTodayBookings(Long employeeId, LocalDate today);
}
