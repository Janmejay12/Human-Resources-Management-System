package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
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
    private List<GameSlot> gameSlots = new ArrayList<>();

    @Column(name = "min_players", nullable = false)
    private int minPlayers;

    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "slot_minutes", nullable = true)
    private Long slotMinutes;

    @Column(name = "operating_start_hours", nullable = true)
    private LocalTime operatingStartHours;

    @Column(name = "operating_end_hours", nullable = true)
    private LocalTime operatingEndHours;
}
