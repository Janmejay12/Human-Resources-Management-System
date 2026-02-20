package com.example.HRMS.repos;

import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotBookingStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSlotRepository extends JpaRepository<BookSlot,Long> {
    boolean existsByGameSlotAndStatus(GameSlot gameSlot, SlotBookingStatuses statuse);
}
