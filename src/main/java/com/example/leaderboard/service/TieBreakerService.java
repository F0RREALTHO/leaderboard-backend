package com.example.leaderboard.service;

import com.example.leaderboard.model.LeaderboardRow;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TieBreakerService implements Comparator<LeaderboardRow> {
    private static final double EPS = 1e-9;

    @Override
    public int compare(LeaderboardRow a, LeaderboardRow b){

        if (Math.abs(a.getTotalPoints() - b.getTotalPoints()) > EPS) {
            return Double.compare(b.getTotalPoints(), a.getTotalPoints());
        }

        if (Math.abs(a.getTotalSpending() - b.getTotalSpending()) > EPS) {
            return Double.compare(a.getTotalSpending(), b.getTotalSpending());
        }

        List<Double> scoresA = new ArrayList<>(a.getEventPoints());
        List<Double> scoresB = new ArrayList<>(b.getEventPoints());

        scoresA.sort(Collections.reverseOrder());
        scoresB.sort(Collections.reverseOrder());

        int maxLen = Math.max(scoresA.size(), scoresB.size());
        for (int i = 0; i < maxLen; i++) {
            double sA = i<scoresA.size() ? scoresA.get(i) : 0.0;
            double sB= i<scoresB.size() ? scoresB.get(i) : 0.0;

            if (Math.abs(sA - sB) > EPS) {
                return Double.compare(sB, sA);
            }
        }
        return a.getPlayerName().compareToIgnoreCase(b.getPlayerName());
    }
}
