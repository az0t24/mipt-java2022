package ru.khairullin.ar.request;

import org.json.JSONException;
import org.json.JSONObject;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.khairullin.ar.data.CreateExcel.generateExcel;

public class request3 {
    private static final String sql = "WITH shortest_path AS (\n" +
            "             SELECT DISTINCT airport.city, min(flight.scheduled_arrival - flight.scheduled_departure) as shortest\n" +
            "             FROM flight INNER JOIN airport ON flight.departure_airport = airport.airport_code\n" +
            "             WHERE flight.ACTUAL_ARRIVAL IS NOT NULL GROUP BY airport.city ORDER BY shortest\n" +
            "         )\n" +
            "SELECT DISTINCT from_airport.city AS departure, to_airport.city AS arrival,\n" +
            "                (flight.scheduled_arrival - flight.scheduled_departure) AS scheduled,\n" +
            "                AVG(DATEDIFF(SS, flight.scheduled_departure, flight.scheduled_arrival)) AS avg_time\n" +
            "FROM flight\n" +
            "         INNER JOIN airport AS from_airport ON flight.departure_airport = from_airport.airport_code\n" +
            "         INNER JOIN airport AS to_airport ON flight.arrival_airport = to_airport.airport_code\n" +
            "WHERE (from_airport.city, (flight.scheduled_arrival - flight.scheduled_departure)) IN (SELECT * FROM shortest_path)\n" +
            "GROUP BY from_airport.city, to_airport.city, (flight.scheduled_arrival - flight.scheduled_departure);";

    public static Map<ArrayList<String>, Integer> getResult(SimpleJdbcTemplate source) throws SQLException {
        Map<ArrayList<String>, Integer> res = new HashMap<ArrayList<String>, Integer>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                JSONObject cityFromJSON = new JSONObject(rs.getString("departure"));
                String cityFrom = cityFromJSON.getString("en");
                JSONObject cityToJSON = new JSONObject(rs.getString("arrival"));
                String cityTo = cityToJSON.getString("en");
                Integer time = rs.getInt("avg_time");
                ArrayList<String> newFlight = new ArrayList<String>();
                newFlight.add(cityFrom);
                newFlight.add(cityTo);
                res.put(newFlight, time);
            }

            generateExcel(rs, "third_report.xlsx");
        });

        return res;
    }

    public static void doRequest(SimpleJdbcTemplate source) throws SQLException {
        Map<ArrayList<String>, Integer> result = request3.getResult(source).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String column1 = "City of departure";
        String column2 = "City of arrival";
        String column3 = "Average time";
        System.out.printf("%-20s | %-20s | %-20s\n", column1, column2, column3);
        System.out.println("---------------------|----------------------|-----------------");
        for (ArrayList<String> cities : result.keySet()) {
            System.out.printf("%-20s | %-20s | %-20d\n", cities.get(0), cities.get(1), result.get(cities));
        }
        System.out.println("--------------------------------------------------------------\n");
    }
}
