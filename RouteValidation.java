package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RouteValidation {

    public static void main(String[] args) {
        try {
            // Load CSV data
            List<String[]> data = loadCSV("C:\\Users\\nandi\\OneDrive\\Pictures\\Screenshots\\documents\\TUM\\4th Sem\\BPC\\Parker\\airroutes-ttl.csv");

            // Define constraints and validate data
            List<String> violations = new ArrayList<>();
            long startTime = System.currentTimeMillis();

            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                validateRow(row, data, i, violations);
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Output violations and runtime
            outputViolations(violations);
            System.out.println("Validation runtime: " + duration + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> loadCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                data.add(fields);
            }
        }
        return data;
    }

    private static void validateRow(String[] row, List<String[]> data, int rowIndex, List<String> violations) {
        // Define field indices based on your CSV structure
        int routeIdIdx = 0;
        int sourceIdx = 1;
        int destinationIdx = 2;
        int cityIdx = 3;
        int countryIdx = 4;
        int codeSharedIdx = 5;
        int airlineOperationalStatusIdx = 6;
        int airlineLabelIdx = 7;
        int airlineIdIdx = 8;

        int routeId = Integer.parseInt(row[routeIdIdx]);
        String source = row[sourceIdx];
        String destination = row[destinationIdx];
        String city = row[cityIdx];
        String country = row[countryIdx];
        boolean codeShared = Boolean.parseBoolean(row[codeSharedIdx]);
        String airlineOperationalStatus = row[airlineOperationalStatusIdx];
        String airlineLabel = row[airlineLabelIdx];
        String airlineId = row[airlineIdIdx];

        // Apply domain rules
        if (source.equals(destination)) {
            violations.add("Violation: Source and destination are the same - " + Arrays.toString(row));
        }
        if (city.equals(country)) {
            violations.add("Violation: City and country are the same - " + Arrays.toString(row));
        }
        if (!Arrays.asList("true", "false").contains(String.valueOf(codeShared))) {
            violations.add("Violation: Invalid code_shared value - " + Arrays.toString(row));
        }
        if (!Arrays.asList("operational", "non-operational").contains(airlineOperationalStatus)) {
            violations.add("Violation: Invalid airline operational status - " + Arrays.toString(row));
        }
        if (airlineLabel.equals("unknown")) {
            violations.add("Violation: Airline label is 'unknown' - " + Arrays.toString(row));
        }

        // Apply sigma rules
        for (int i = rowIndex + 1; i < data.size(); i++) {
            String[] otherRow = data.get(i);
            int otherRouteId = Integer.parseInt(otherRow[routeIdIdx]);
            String otherAirlineId = otherRow[airlineIdIdx];
            String otherAirlineOperationalStatus = otherRow[airlineOperationalStatusIdx];
            boolean otherCodeShared = Boolean.parseBoolean(otherRow[codeSharedIdx]);

            if (routeId != otherRouteId && airlineId.equals(otherAirlineId) &&
                    airlineOperationalStatus.equals("operational") && !codeShared) {
                violations.add("Violation: Conflicting routes with the same airline ID - " +
                        Arrays.toString(row) + " and " + Arrays.toString(otherRow));
            }
        }
    }

    private static void outputViolations(List<String> violations) {
        if (violations.isEmpty()) {
            System.out.println("No violations found.");
        } else {
            for (String violation : violations) {
                System.out.println(violation);
            }
        }
    }
}
