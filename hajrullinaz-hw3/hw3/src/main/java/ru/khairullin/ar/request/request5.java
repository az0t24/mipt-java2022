package ru.khairullin.ar.request;

import org.json.JSONException;
import org.json.JSONObject;
import ru.khairullin.ar.data.DrawDiagram;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.khairullin.ar.data.CreateExcel.generateExcel;

public class request5 {
    private static final Map<Integer, String> numToDay = Map.of(
            1, "Monday",
            2, "Tuesday",
            3, "Wednesday",
            4, "Thursday",
            5, "Friday",
            6, "Saturday",
            7, "Sunday");
    private static final String sql = "WITH airports_msk AS (\n" +
            "             SELECT DISTINCT airport_code FROM airport WHERE airport.city = '{\"en\": \"Moscow\", \"ru\": \"Москва\"}')\n" +
            "SELECT toMsk.dayweek, toMsk.cntto, fromMsk.cntfrom\n" +
            "FROM (SELECT ISO_DAY_OF_WEEK(scheduled_arrival) AS dayweek, count(*) AS cntto\n" +
            "        FROM flight WHERE arrival_airport IN (SELECT * FROM airports_msk) GROUP BY dayweek\n" +
            "    ) AS toMsk\n" +
            "    INNER JOIN (\n" +
            "        SELECT ISO_DAY_OF_WEEK(scheduled_departure) AS dayweek, count(*) AS cntfrom\n" +
            "        FROM flight WHERE departure_airport IN (SELECT * FROM airports_msk) GROUP BY dayweek\n" +
            "    ) AS fromMsk\n" +
            "    ON toMsk.dayweek = fromMsk.dayweek;";

    public static Map<String, ArrayList<Integer>> getResult(SimpleJdbcTemplate source) throws SQLException {
        Map<String, ArrayList<Integer>> res = new HashMap<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String month = numToDay.get(rs.getInt("dayWeek"));
                Integer timesTo = rs.getInt("cntTo");
                Integer timesFrom = rs.getInt("cntFrom");
                ArrayList<Integer> times = new ArrayList<>();
                times.add(timesTo);
                times.add(timesFrom);
                res.put(month, times);
            }

            generateExcel(rs, "fifth_report.xlsx");
        });

        return res;
    }

    public static void doRequest(SimpleJdbcTemplate source) throws SQLException {
        Map<String, ArrayList<Integer>> result = request5.getResult(source);

        String column1 = "Day of the week";
        String column2 = "Flights to";
        String column3 = "Flight from";
        System.out.printf("%-20s | %-20s | %-20s\n", column1, column2, column3);
        System.out.println("---------------------|----------------------|---------");
        Map<String, Integer> toDrawTo = new HashMap<>();
        Map<String, Integer> toDrawFrom = new HashMap<>();
        for (String day : result.keySet()) {
            toDrawTo.put(day, result.get(day).get(0));
            toDrawFrom.put(day, result.get(day).get(1));
            System.out.printf("%-20s | %-20d | %-20d\n", day, result.get(day).get(0), result.get(day).get(1));
        }
        System.out.println("----------------------------------------------------\n");

        DrawDiagram.draw(toDrawTo);
        DrawDiagram.draw(toDrawFrom);
    }
}
