package com.example.leaderboard.model;

import lombok.Data;

import java.util.List;

@Data
public class MovementResult {
    private String playerName;
    private List<Integer> ranks;
    private String finalMovement;
}
