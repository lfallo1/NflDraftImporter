package com.combine.service;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class JSONService {

    private static final Logger logger = Logger.getLogger(JSONService.class);

    public Integer getIntFromJSON(JSONObject obj, String key) {
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public Double getDoubleFromJSON(JSONObject obj, String key) {
        try {
            return obj.getDouble(key);
        } catch (JSONException e) {
            return 0.0;
        }
    }

    public String getStringFromJSON(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public String loadJson(String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new ClassPathResource(filename).getFile());
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }

            scanner.close();
        } catch (Exception e) {
            logger.warn(e.toString());
        }
        return sb.toString();
    }

    public void jsonToExcel(String inputFile, String outputFile, String primaryObject, List<String> ignoreList) throws IOException {
        Map<String, Integer> columnMap = new HashMap<>();
        String data = this.loadJson(inputFile);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(primaryObject);

        JSONArray freeAgentsArray = new JSONObject(data).getJSONArray(primaryObject);

        //create header row, and column map
        Row header = sheet.createRow(0);
        JSONArray fields = freeAgentsArray.getJSONObject(0).names();
        int idx = 0;
        for (int i = 0; i < fields.length(); i++) {
            if (!ignoreList.contains(fields.getString(i))) {
                header.createCell(idx).setCellValue(fields.getString(i));
                columnMap.put(fields.getString(i), idx);
                idx++;
            }
        }

        //iterate over each object in the array, create a new row for each object,
        //and use the column mapper to set cell values at appropriate column index
        for (int i = 0; i < freeAgentsArray.length(); i++) {
            Row row = sheet.createRow(i + 1);
            JSONObject player = freeAgentsArray.getJSONObject(i);
            for (int j = 0; j < fields.length(); j++) {
                if (!ignoreList.contains(fields.getString(j))) {
                    try {
                        Cell cell = row.createCell(columnMap.get(fields.getString(j)));

                        if (player.get(fields.getString(j)) instanceof String) {
                            cell.setCellValue(player.getString(fields.getString(j)));
                        } else if (player.get(fields.getString(j)) instanceof Boolean) {
                            cell.setCellValue(player.getBoolean(fields.getString(j)));
                        } else if (player.get(fields.getString(j)) instanceof Double) {
                            cell.setCellValue(player.getDouble(fields.getString(j)));
                        } else {
                            cell.setCellValue("");
                        }
                    } catch (JSONException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        //write to file
        FileOutputStream fos = new FileOutputStream(new File(outputFile));
        workbook.write(fos);
        fos.close();
    }

    public JSONObject findByKey(JSONObject prospects, String playerId) {
        for (Object key : prospects.keySet()) {
            if (key.toString().equals(playerId)) {
                return prospects.getJSONObject(key.toString());
            }
        }
        return null;
    }
}
