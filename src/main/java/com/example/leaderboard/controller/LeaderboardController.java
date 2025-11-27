package com.example.leaderboard.controller;

import com.example.leaderboard.model.MovementResult;
import com.example.leaderboard.model.RankResult;
import com.example.leaderboard.service.RankingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController {
    private final RankingService rankingService;

    @Autowired
    public LeaderboardController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/leaderboard")
    public List<RankResult> getLeaderboard() {
        return rankingService.generateLeaderboard();
    }

    @GetMapping("/leaderboard/event/{eventNumber}")
    public List<RankResult> getLeaderboardUntil(@PathVariable int eventNumber) {
        return rankingService.generateLeaderboardUntil(eventNumber);
    }

    @GetMapping("/leaderboard/movement")
    public List<MovementResult> getRankingMovement() {
        return rankingService.getRankMovements();
    }
}
