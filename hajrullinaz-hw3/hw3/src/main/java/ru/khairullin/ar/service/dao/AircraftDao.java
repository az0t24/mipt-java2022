package ru.khairullin.ar.service.dao;

import ru.khairullin.ar.domain.Aircraft;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AircraftDao {
    private final SimpleJdbcTemplate source;

    private Aircraft createAircraft(ResultSet resultSet) throws SQLException {
        return new Aircraft(
                resultSet.getString("aircraft_code"),
                resultSet.getString("model"),
                resultSet.getInt("range")
        );
    }

    public void saveAircrafts(Collection<Aircraft> aircrafts) throws SQLException {
        source.preparedStatement("INSERT INTO aircraft(aircraft_code, model, range) VALUES (?, ?, ?)", insertAircraft -> {
            for (Aircraft aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getModel());
                insertAircraft.setInt(3, aircraft.getRange());
                insertAircraft.execute();
            }
        });
    }

    public Set<Aircraft> getAircrafts() throws SQLException {
        return source.statement((stmt) -> {
            Set<Aircraft> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM aircraft");

            while (resultSet.next()) {
                result.add(this.createAircraft(resultSet));
            }

            return result;
        });
    }
}
