package com.example.HRMS.schedulers;

import com.example.HRMS.entities.BookSlot;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotBookingStatuses;
import com.example.HRMS.enums.SlotStatuses;
import com.example.HRMS.repos.BookSlotRepository;
import com.example.HRMS.repos.GameSlotRepository;
import com.example.HRMS.services.BookSlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlotPromotionScheduler {
    private final GameSlotRepository gameSlotRepository;
    private final BookSlotService bookSlotService;
    private final BookSlotRepository bookSlotRepository;

    @Scheduled(cron = "0 10/10 * * * ?")
    @Transactional
    public void promoteSlot(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime cutoffTime = now.plusMinutes(22);


        log.info("Running slot promotion scheduler at {}", now);

        List<GameSlot> slots = gameSlotRepository.findSlotsForPromotion(today,now,cutoffTime);

        for(GameSlot slot : slots){
            LocalTime threshold = slot.getStartTime().minusMinutes(20);

            if(now.isAfter(threshold)
                    && now.isBefore(slot.getStartTime())){
                bookSlotService.promoteNextIfAvailable(slot);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetSlotsAtMidnight() {

        LocalDate today = LocalDate.now();

        log.info("Running midnight slot reset for date {}", today);

        List<GameSlot> allSlots = gameSlotRepository.findAll();

        for (GameSlot slot : allSlots) {

            // STEP 1: Update slot date
            slot.setSlotDate(today);

            // STEP 2: Reset slot status
            slot.setStatus(SlotStatuses.EMPTY);

            // STEP 3: Remove all bookings linked to this slot
            List<BookSlot> bookings =
                    bookSlotRepository.findByGameSlotAndIsDeletedFalse(slot);

            for (BookSlot booking : bookings) {

                booking.setDeleted(true);
                booking.setStatus(SlotBookingStatuses.CANCELLED);

            }

            bookSlotRepository.saveAll(bookings);
        }

        gameSlotRepository.saveAll(allSlots);

        log.info("Midnight slot reset completed");
    }
}
