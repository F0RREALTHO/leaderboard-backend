package com.example.leaderboard.service;

import com.example.leaderboard.model.LeaderboardRow;
import com.example.leaderboard.model.MovementResult;
import com.example.leaderboard.model.RankResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankingService {
    private final ExcelLoaderService excelLoaderService;
    private final TieBreakerService tieBreakerService;

    public RankingService(ExcelLoaderService excelLoaderService, TieBreakerService tieBreakerService) {
        this.excelLoaderService = excelLoaderService;
        this.tieBreakerService = tieBreakerService;
    }

    public List<MovementResult> getRankMovements(){
        Map<String,List<Double>> pointsMap = excelLoaderService.loadPoints();

        Map<String, List<Integer>> movementMap = new LinkedHashMap<>();
        for (String player : pointsMap.keySet()) {
            movementMap.put(player, new ArrayList<>());
        }

        for (int event = 1; event <= 24; event++) {
            List<RankResult> leaderboard = generateLeaderboardUntil(event);

            for (RankResult result : leaderboard) {
                movementMap.get(result.getPlayerName()).add(result.getRank());
            }
        }
        List<MovementResult> movementResults = new ArrayList<>();

        for(String player : movementMap.keySet()){
            List<Integer> ranks = movementMap.get(player);

            MovementResult mr = new MovementResult();
            mr.setPlayerName(player);
            mr.setRanks(ranks);

            int first = ranks.get(0);
            int last = ranks.get(ranks.size() - 1);

            String movement;
            if (last < first) movement = "+" + (first - last);
            else if (last > first) movement = "-" + (last - first);
            else movement = "0";

            mr.setFinalMovement(movement);

            movementResults.add(mr);
        }
        return movementResults;
    }

    public List<RankResult> generateLeaderboardUntil(int eventNumber) {
        if (eventNumber < 1 || eventNumber > 24) {
            throw new IllegalArgumentException("Event number must be between 1 and 24");
        }
        Map<String, List<Double>> pointsMap = excelLoaderService.loadPoints();
        Map<String, List<Double>> spendingMap = excelLoaderService.loadSpending();

        List<LeaderboardRow> rows = new ArrayList<>();

        for(String player:pointsMap.keySet()) {
            List<Double> eventPoints = pointsMap.get(player);
            List<Double> eventSpending = spendingMap.get(player);

            List<Double> slicedPoints = eventPoints.subList(0, eventNumber);
            List<Double> slicedSpending = eventSpending.subList(0, eventNumber);

            double totalPoints = slicedPoints.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            double totalSpending = slicedSpending.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            LeaderboardRow row = new LeaderboardRow();
            row.setPlayerName(player);
            row.setEventPoints(slicedPoints);
            row.setEventSpending(slicedSpending);
            row.setTotalPoints(totalPoints);
            row.setTotalSpending(totalSpending);

            rows.add(row);
        }
        rows.sort(tieBreakerService);

        rows.sort(tieBreakerService);

        List<RankResult> results = new ArrayList<>();
        int rank = 1;

        for (LeaderboardRow row : rows) {
            RankResult result = new RankResult();
            result.setRank(rank++);
            result.setPlayerName(row.getPlayerName());
            result.setTotalPoints(row.getTotalPoints());
            result.setTotalSpending(row.getTotalSpending());
            results.add(result);
        }
        return results;
    }

    public List<RankResult> generateLeaderboard(){
        Map<String,List<Double>> pointsMap = excelLoaderService.loadPoints();
        Map<String,List<Double>> spendingMap = excelLoaderService.loadSpending();

        List<LeaderboardRow> rows = new ArrayList<>();

        for(String player:pointsMap.keySet()) {
            List<Double> eventPoints = pointsMap.get(player);
            List<Double> eventSpending = spendingMap.get(player);

            if (eventSpending == null) {
                throw new RuntimeException("Spending for player " + player + " not found");
            }

            double totalPoints = eventPoints.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double totalSpending = eventSpending.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            LeaderboardRow row = new LeaderboardRow();
            row.setPlayerName(player);
            row.setEventPoints(eventPoints);
            row.setEventSpending(eventSpending);
            row.setTotalPoints(totalPoints);
            row.setTotalSpending(totalSpending);

            rows.add(row);
        }
        rows.sort(tieBreakerService);

        List<RankResult> results = new ArrayList<>();
        int rank = 1;

        for (LeaderboardRow row : rows) {
            RankResult result = new RankResult();
            result.setRank(rank++);
            result.setPlayerName(row.getPlayerName());
            result.setTotalPoints(row.getTotalPoints());
            result.setTotalSpending(row.getTotalSpending());

            results.add(result);
        }

        return results;
    }

}
