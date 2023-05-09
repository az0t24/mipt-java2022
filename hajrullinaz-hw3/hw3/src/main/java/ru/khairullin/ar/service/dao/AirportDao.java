package ru.khairullin.ar.service.dao;

import ru.khairullin.ar.domain.Airport;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AirportDao {
    private final SimpleJdbcTemplate source;

    private Airport createAirport(ResultSet resultSet) throws SQLException {
        return new Airport(
                resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone")
        );
    }

    public void saveAirports(Collection<Airport> airports) throws SQLException {
        this.source.preparedStatement("insert into airport(" +
                "airport_code, airport_name, city, coordinates, timezone) values (?, ?, ?, ?, ?)", insertAirport -> {
            for (Airport airport : airports) {
                insertAirport.setString(1, airport.getAirportCode());
                insertAirport.setString(2, airport.getAirportName());
                insertAirport.setString(3, airport.getCity());
                insertAirport.setString(4, airport.getCoordinates());
                insertAirport.setString(5, airport.getTimezone());
                insertAirport.execute();
            }

        });
    }

    public Set<Airport> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airport> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM airport");

            while(resultSet.next()) {
                result.add(this.createAirport(resultSet));
            }

            return result;
        });
    }
}
