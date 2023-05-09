package ru.khairullin.ar.request;

import org.json.JSONException;
import org.json.JSONObject;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.khairullin.ar.data.CreateExcel.generateExcel;

public class request2 {
    private static final String sql = "SELECT city as city, count(*) as cnt\n" +
            "FROM flight INNER JOIN airport\n" +
            "                        ON flight.departure_airport = airport.airport_code\n" +
            "WHERE flight.status = 'Cancelled'\n" +
            "GROUP BY city\n" +
            "ORDER BY cnt DESC\n" +
            "LIMIT 5;";

    public static Map<String, Integer> getResult(SimpleJdbcTemplate source) throws SQLException {
        Map<String, Integer> res = new HashMap<String, Integer>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                JSONObject cityJSON = new JSONObject(rs.getString("city"));
                String city = cityJSON.getString("en");
                Integer count = rs.getInt("cnt");
                res.put(city, count);
            }

            generateExcel(rs, "second_report.xlsx");
        });

        return res;
    }

    public static void doRequest(SimpleJdbcTemplate source) throws SQLException {
        Map<String, Integer> result = request2.getResult(source).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String column1 = "City";
        String column2 = "Cancelled";
        System.out.printf("%-20s | %-20s\n", column1, column2);
        System.out.println("---------------------|-------------");
        for (String city : result.keySet()) {
            System.out.printf("%-20s | %-20d\n", city, result.get(city));
        }
        System.out.println("-----------------------------------\n");
    }
}
