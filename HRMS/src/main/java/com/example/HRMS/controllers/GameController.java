package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.UpdateGameRequest;
import com.example.HRMS.dtos.response.ExpenseResponse;
import com.example.HRMS.dtos.response.GameResponse;
import com.example.HRMS.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGame(    @RequestBody UpdateGameRequest request, @PathVariable Long id){
        try{
            GameResponse gameResponse = gameService.updateGame(request,id);
            return ResponseEntity.ok(gameResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping()
    public ResponseEntity<?> getAllGames(){
        try{

            return ResponseEntity.ok(gameService.getAllGames());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/game-slots")
    public ResponseEntity<?> getAllSlots(@PathVariable Long id){
        try{

            return ResponseEntity.ok(gameService.getAllSlots(id));
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
