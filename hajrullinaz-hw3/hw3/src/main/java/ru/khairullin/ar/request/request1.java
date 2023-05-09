package ru.khairullin.ar.request;

import org.json.JSONException;
import org.json.JSONObject;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ru.khairullin.ar.data.CreateExcel.generateExcel;

public class request1 {
    private static final String sql = "SELECT city, airports FROM (SELECT city as city, STRING_AGG(airport_code, ',') as"
        + " airports, count(*) as cnt FROM airport GROUP BY city) as helper WHERE cnt > 1;";

    public static Map<String, String> getResult(SimpleJdbcTemplate source) throws SQLException {
        Map<String, String> res = new HashMap<String, String>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                JSONObject cityJSON = new JSONObject(rs.getString("city"));
                String city = cityJSON.getString("en");
                String airports = rs.getString("airports");
                res.put(city, airports);
            }

            generateExcel(rs, "first_report.xlsx");
        });

        return res;
    }

    public static void doRequest(SimpleJdbcTemplate source) throws SQLException {
        Map<String, String> result = request1.getResult(source);

        String column1 = "City";
        String column2 = "Airports";
        System.out.printf("%-20s | %-20s\n", column1, column2);
        System.out.println("---------------------|-------------");
        for (String city : result.keySet()) {
            System.out.printf("%-20s | %-20s\n", city, result.get(city));
        }
        System.out.println("-----------------------------------\n");
    }
}
