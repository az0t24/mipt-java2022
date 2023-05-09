package ru.khairullin.ar.request;

import org.json.JSONException;
import org.json.JSONObject;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class request6 {
    public static void getResult(SimpleJdbcTemplate source, String model) throws SQLException {
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery("WITH deleted AS (\n" +
                    "    SELECT AIRCRAFT_CODE from AIRCRAFT WHERE MODEL LIKE " + model + "\n" +
                    ")\n" +
                    "UPDATE FLIGHT\n" +
                    "SET status='Cancelled' WHERE aircraft_code IN (SELECT * FROM deleted);\n" +
                    "WITH deleted AS (\n" +
                    "    SELECT AIRCRAFT_CODE from AIRCRAFT WHERE MODEL LIKE " + model + "\n" +
                    ")\n" +
                    "DELETE FROM ticket WHERE ticket.ticket_no IN (SELECT ticket_no FROM TICKET_FLIGHT AS tick_fl, FLIGHT AS fl\n" +
                    "                                        WHERE fl.FLIGHT_ID LIKE tick_fl.FLIGHT_ID AND fl.AIRCRAFT_CODE IN (SELECT * FROM deleted));");
        });
    }

    public static void doRequest(SimpleJdbcTemplate source, String model) throws SQLException {
        System.out.println("Start deleting of all flights of model " + model);
        request6.getResult(source, model);
        System.out.println("Deleted\n");
    }
}
