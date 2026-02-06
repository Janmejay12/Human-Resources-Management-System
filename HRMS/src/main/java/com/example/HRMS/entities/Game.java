package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(name = "game_name", nullable = false)
    private String gameName;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<BookSlot> bookSlots = new ArrayList<>();

    @Column(name = "min_players", nullable = false)
    private int minPlayers;

    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "slot_minutes", nullable = false)
    private int slotMinutes;

    @Column(name = "operating_hours", nullable = false)
    private double operatingHours;

}
