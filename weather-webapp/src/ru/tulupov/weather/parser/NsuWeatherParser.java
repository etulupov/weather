package ru.tulupov.weather.parser;

import ru.tulupov.weather.model.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NsuWeatherParser {

    private String data;

    enum State {
        PARSE_X_POINTS,
        PARSE_Y_POINTS,
        PARSE_X_DAYS,
        PARSE_Y_GRID,
        FIND_X_GRID_MARKER,
        PARSE_X_GRID,
        PARSE_CURRENT_TEMPERATURE,
        PARSE_AVERAGE_TEMPERATURE,


    }

    public NsuWeatherParser(String data) {
        this.data = data;
    }

    public Weather parse() {
        Weather weather = new Weather();

        Scanner scanner = new Scanner(data);
        State state = State.PARSE_X_POINTS;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();


            switch (state) {
                case PARSE_X_POINTS:
                    if (line.startsWith("document.Xpoints")) {
                        state = State.PARSE_Y_POINTS;
//                        System.out.println(parsePointArray(line));
                    }
                    break;

                case PARSE_Y_POINTS:
                    if (line.startsWith("document.Ypoints")) {
                        state = State.FIND_X_GRID_MARKER;
//                        System.out.println(parsePointArray(line));
                    }
                    break;
                case FIND_X_GRID_MARKER:
                    if (line.contains("graph.setStroke(1);")) {
                        state = State.PARSE_X_GRID;
                    }
                    break;
                case PARSE_X_GRID:
                    final String TIME_POS_START_PATTERN = "graph.drawLine(";
                    final String TIME_POS_STOP_PATTERN = ",";
                    if (line.contains(TIME_POS_START_PATTERN)) {
                        line = line.substring(line.indexOf(TIME_POS_START_PATTERN) + TIME_POS_START_PATTERN.length());
                        int xCoord = Integer.parseInt(line.substring(0, line.indexOf(TIME_POS_STOP_PATTERN)));
//                        System.out.println(xCoord);
                    }

                    if (line.contains("graph.setStroke(2);")) {
                        state = State.PARSE_X_DAYS;
                    }

                    break;
                case PARSE_X_DAYS:
                    state = State.PARSE_Y_GRID;
                    break;
                case PARSE_Y_GRID:
                    final String TEMP_POS_START_PATTERN = "graph.drawLine(0, ";
                    final String TEMP_POS_STOP_PATTERN = ",";
                    final String TEMP_START_PATTERN = "font-weight: bold;\">";
                    final String TEMP_STOP_PATTERN = "</div>";
                    if (line.contains(TEMP_POS_START_PATTERN)) {
                        line = line.substring(line.indexOf(TEMP_POS_START_PATTERN) + TEMP_POS_START_PATTERN.length());
                        int yCoord = Integer.parseInt(line.substring(0, line.indexOf(TEMP_POS_STOP_PATTERN)));
                        line = scanner.nextLine();

                        line = line.substring(line.indexOf(TEMP_START_PATTERN) + TEMP_START_PATTERN.length());
                        int temp = Integer.parseInt(line.substring(0, line.indexOf(TEMP_STOP_PATTERN)));
//                        System.out.println(yCoord + " " + temp);
                    }

                    if (line.contains("id = 'temp';")) {
                        state = State.PARSE_CURRENT_TEMPERATURE;
                    }

                    break;
                case PARSE_CURRENT_TEMPERATURE:
                    final String CURR_TEMP_START_PATTERN = "if(cnv) cnv.innerHTML = '";
                    final String CURR_TEMP_STOP_PATTERN = "&";

                    if (line.contains(CURR_TEMP_START_PATTERN)) {
                        line = line.substring(line.indexOf(CURR_TEMP_START_PATTERN) + CURR_TEMP_START_PATTERN.length());
                        double temp = Double.parseDouble(line.substring(0, line.indexOf(CURR_TEMP_STOP_PATTERN)));
                        weather.setCurrentTemperature(temp);
                        state = State.PARSE_AVERAGE_TEMPERATURE;
                    }
                    break;
                case PARSE_AVERAGE_TEMPERATURE:
                    final String AVG_TEMP_START_PATTERN = "if(cnv) cnv.innerHTML = '";
                    final String AVG_TEMP_STOP_PATTERN = "&";
                    if (line.contains(AVG_TEMP_START_PATTERN)) {
                        line = line.substring(line.indexOf(AVG_TEMP_START_PATTERN) + AVG_TEMP_START_PATTERN.length());
                        double temp = Double.parseDouble(line.substring(0, line.indexOf(AVG_TEMP_STOP_PATTERN)));
                    }
                    break;
            }


        }

        return weather;
    }

    private List<Integer> parsePointArray(String line) {
        List<Integer> list = new ArrayList<Integer>();

        line = line.substring(line.indexOf("[") + 1);
        line = line.replace("];", "");
        String[] values = line.split(",");
        for (String value : values) {
            list.add(Integer.valueOf(value.trim()));
        }

        return list;
    }
}
