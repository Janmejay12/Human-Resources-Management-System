package com.example.HRMS.repos;

import com.example.HRMS.entities.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot,Long> {

    @Query("DELETE FROM GameSlot gs WHERE gs.game.gameId = :gameId")
    void deleteGameSlotsByGameId(@Param("gameId") Long gameId);
}
