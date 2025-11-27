package com.example.leaderboard.model;

import lombok.Data;

import java.util.List;

@Data
public class LeaderboardRow {
    private String playerName;
    private List<Double> eventPoints;
    private List<Double> eventSpending;

    private double totalPoints;
    private double totalSpending;
}
