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

public class request4 {
    private static final Map<Integer, String> numToMonth = new HashMap<>();
    static {
        numToMonth.put(1, "January");
        numToMonth.put(2, "February");
        numToMonth.put(3, "March");
        numToMonth.put(4, "April");
        numToMonth.put(5, "May");
        numToMonth.put(6, "June");
        numToMonth.put(7, "July");
        numToMonth.put(8, "August");
        numToMonth.put(9, "September");
        numToMonth.put(10, "October");
        numToMonth.put(11, "November");
        numToMonth.put(12, "December");
    }
    private static final String sql = "SELECT MONTH(scheduled_departure) as monthCancell, count(*) as numCancelled FROM flight WHERE status = 'Cancelled' GROUP BY monthCancell;";

    public static Map<String, Integer> getResult(SimpleJdbcTemplate source) throws SQLException {
        Map<String, Integer> res = new HashMap<String, Integer>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String month = numToMonth.get(rs.getInt("monthCancell"));
                Integer times = rs.getInt("numCancelled");
                res.put(month, times);
            }

            generateExcel(rs, "fourth_report.xlsx");
        });

        return res;
    }

    public static void doRequest(SimpleJdbcTemplate source) throws SQLException {
        Map<String, Integer> result = request4.getResult(source);

        String column1 = "Month";
        String column2 = "Total cancelled flights";
        System.out.printf("%-20s | %-20s\n", column1, column2);
        System.out.println("---------------------|---------------------");
        for (String month : result.keySet()) {
            System.out.printf("%-20s | %-20d\n", month, result.get(month));
        }
        System.out.println("-------------------------------------------\n");

        DrawDiagram.draw(result);
    }
}
