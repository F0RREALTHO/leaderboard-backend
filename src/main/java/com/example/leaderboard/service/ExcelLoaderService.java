package com.example.leaderboard.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;


@Service
public class ExcelLoaderService {
    private static final int TOP_TABLE_START = 2;
    private static final int TOP_TABLE_END = 26;
    private static final int BOTTOM_TABLE_START = 32;
    private static final int BOTTOM_TABLE_END = 56;

    private static final int PLAYER_NAME_COL = 1;
    private static final int EVENT_START_COL = 2;
    private static final int EVENT_END_COL = 25;

    public Map<String, List<Double>> loadPoints(){
        Map<String, List<Double>> pointsMap = new LinkedHashMap<>();

        try(InputStream is = loadFile("leaderboard.xlsx");
            Workbook workbook = new XSSFWorkbook(is)){

            Sheet sheet = workbook.getSheetAt(0);
            for(int rowIndex = TOP_TABLE_START; rowIndex <= TOP_TABLE_END; rowIndex++){
                Row row = sheet.getRow(rowIndex);
                if(row == null){continue;}

                Cell namecell = row.getCell(PLAYER_NAME_COL);
                if(namecell == null){continue;}

                String playername = namecell.getStringCellValue().trim();
                if(playername.isBlank()){continue;}

                List<Double> eventScores = new ArrayList<>();

                for(int col =  EVENT_START_COL; col <= EVENT_END_COL; col++){
                    Cell cell = row.getCell(col);
                    eventScores.add(parsePointsCell(cell));
                }
                pointsMap.put(playername, eventScores);
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to load points table from Excel", e);
        }
        return pointsMap;
    }

    public Map<String,List<Double>> loadSpending(){
        Map<String, List<Double>> spendingMap = new LinkedHashMap<>();

        try (InputStream is = loadFile("leaderboard.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for(int rowIndex = BOTTOM_TABLE_START; rowIndex <= BOTTOM_TABLE_END; rowIndex++){
                Row row = sheet.getRow(rowIndex);
                if(row == null){continue;}

                Cell namecell = row.getCell(PLAYER_NAME_COL);
                if(namecell == null){continue;}

                String playername = namecell.getStringCellValue().trim();
                if(playername.isBlank()){continue;}

                List<Double> spendingValues = new ArrayList<>();

                for(int col =  EVENT_START_COL; col <= EVENT_END_COL; col++){
                    Cell cell = row.getCell(col);
                    spendingValues.add(parseSpendingCell(cell));
                }
                spendingMap.put(playername, spendingValues);
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to load spending from Excel", e);
        }
        return spendingMap;
    }

    private Double parseSpendingCell(Cell cell) {
        if(cell == null){return 0.0;}

        switch (cell.getCellType()){
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String text = cell.getStringCellValue().trim();
                if(text.isEmpty() || text.equals("-")){return 0.0;}
                try{
                    return Double.parseDouble(text);
                }catch (NumberFormatException ignored){return 0.0;}
            case FORMULA:
                try {
                    FormulaEvaluator eval = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue value = eval.evaluate(cell);
                    if (value != null && value.getCellType() == CellType.NUMERIC) {
                        return value.getNumberValue();
                    }
                } catch (Exception ignored) {
                }
                return 0.0;
            default:return 0.0;
        }
    }

    private Double parsePointsCell(Cell cell) {
        if(cell == null){return 0.0;}

        switch(cell.getCellType()){
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String text = cell.getStringCellValue().trim();
                if(text.equalsIgnoreCase("D$Q")
                        || text.equals("-")
                        || text.isEmpty()){
                    return 0.0;
                }
                try {
                    return Double.parseDouble(text);
                }catch (NumberFormatException ignored){
                    return 0.0;
                }
            case FORMULA:
                try {
                    FormulaEvaluator eval = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    CellValue value = eval.evaluate(cell);
                    if (value != null && value.getCellType() == CellType.NUMERIC) {
                        return value.getNumberValue();
                    }
                } catch (Exception ignored) {
                }
                return 0.0;
            default:
                return 0.0;
        }
    }
    private InputStream loadFile(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new RuntimeException("Excel file not found in resources: " + fileName);
        }
        return is;
    }
}
