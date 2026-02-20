package com.example.HRMS.services;

import com.example.HRMS.dtos.request.UpdateGameRequest;
import com.example.HRMS.dtos.response.GameResponse;
import com.example.HRMS.dtos.response.GameSlotResponse;
import com.example.HRMS.entities.Game;
import com.example.HRMS.entities.GameSlot;
import com.example.HRMS.enums.SlotStatuses;
import com.example.HRMS.repos.GameRepository;
import com.example.HRMS.repos.GameSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameSlotRepository gameSlotRepository;

    public GameService(GameRepository gameRepository,GameSlotRepository gameSlotRepository) {
        this.gameRepository = gameRepository;
        this.gameSlotRepository = gameSlotRepository;
    }

    @Transactional
    public GameResponse updateGame(UpdateGameRequest request, Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with ID: " + gameId));

        game.setSlotMinutes(request.getSlotMinutes());
        game.setOperatingStartHours(request.getOperatingStartHours());
        game.setOperatingEndHours(request.getOperatingEndHours());
        gameRepository.save(game);

        //gameSlotRepository.deleteGameSlotsByGameId(gameId);
        Long i = 1L;

        LocalTime current = game.getOperatingStartHours();
        while (current.plusMinutes(game.getSlotMinutes()).isBefore(game.getOperatingEndHours()) || current.plusMinutes(game.getSlotMinutes()).equals(game.getOperatingEndHours())) {

            GameSlot slot = new GameSlot();
            slot.setGame(game);
            slot.setStartTime(current);
            slot.setSlotNumber(i); i++;
            slot.setStatus(SlotStatuses.EMPTY);
            slot.setSlotDate(LocalDate.now());
            slot.setEndTime(current.plusMinutes(game.getSlotMinutes()));
            gameSlotRepository.save(slot);
            current = current.plusMinutes(game.getSlotMinutes());
        }


        GameResponse gameResponse = new GameResponse();
        gameResponse.setGameId(game.getGameId());
        gameResponse.setGameName(game.getGameName());
        gameResponse.setSlotMinutes(game.getSlotMinutes());
        gameResponse.setOperatingStartHours(game.getOperatingStartHours());
        gameResponse.setOperatingEndHours(game.getOperatingEndHours());
        return gameResponse;
    }

    public List<GameResponse> getAllGames(){
        List<Game> games = gameRepository.findAll();
        List<GameResponse> gameResponses = games.stream().map((game) -> {
            GameResponse gameResponse = new GameResponse();
            gameResponse.setGameId(game.getGameId());
            gameResponse.setGameName(game.getGameName());
            gameResponse.setSlotMinutes(game.getSlotMinutes());
            gameResponse.setOperatingStartHours(game.getOperatingStartHours());
            gameResponse.setOperatingEndHours(game.getOperatingEndHours());
            return gameResponse;
        }).toList();
        return  gameResponses;
    }

    public List<GameSlotResponse> getAllSlots(Long gameId){
        List<GameSlot> gameSlots = gameSlotRepository.findAllByGameId(gameId);
        List<GameSlotResponse> gameSlotResponses = gameSlots.stream().map((gs) -> {
            GameSlotResponse gameSlotResponse = new GameSlotResponse();
            gameSlotResponse.setGameId(gs.getGame().getGameId());
            gameSlotResponse.setGameSlotId(gs.getGameSlotId());
            gameSlotResponse.setSlotNumber(gs.getSlotNumber());
            gameSlotResponse.setSlotDate(gs.getSlotDate());
            gameSlotResponse.setStatus(gs.getStatus());
            gameSlotResponse.setStartTime(gs.getStartTime());
            gameSlotResponse.setEndTime(gs.getEndTime());
            return gameSlotResponse;
        }).toList();
        return  gameSlotResponses;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void changeSlotDateAtMidnight(){
        LocalDate tomorrow = LocalDate.now();
        List<GameSlot> allGameSlots = gameSlotRepository.findAll();

        for(GameSlot gs : allGameSlots){
            gs.setSlotDate(tomorrow);
        }

        gameSlotRepository.saveAll(allGameSlots);
    }


}
