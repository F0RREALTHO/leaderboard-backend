package com.example.leaderboard.model;

import lombok.Data;

@Data
public class RankResult {
    private int rank;
    private String playerName;
    private double totalPoints;
    private double totalSpending;
}
