package com.example.HRMS.repos;

import com.example.HRMS.entities.Expense;
import com.example.HRMS.entities.GameSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot,Long> {

    @Query("DELETE FROM GameSlot gs WHERE gs.game.gameId = :gameId")
    void deleteGameSlotsByGameId(@Param("gameId") Long gameId);

    @Query("SELECT gs FROM GameSlot gs WHERE gs.game.gameId = :gameId")
    List<GameSlot> findAllByGameId(@Param("gameId") Long gameId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM GameSlot s WHERE s.gameSlotId = :id")
    Optional<GameSlot> findByIdForUpdate(Long id);
}
