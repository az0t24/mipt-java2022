package ru.khairullin.ar.service.dao;

import ru.khairullin.ar.domain.Seat;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SeatDao {
    private final SimpleJdbcTemplate source;

    private Seat createSeat(ResultSet resultSet) throws SQLException {
        return new Seat(
                resultSet.getString("aircraft_code"),
                resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions")
        );
    }

    public void saveSeats(Collection<Seat> seats) throws SQLException {
        this.source.preparedStatement("insert into seat(aircraft_code, " +
                "seat_no, fare_conditions) values (?, ?, ?)", (insertSeat) -> {
            for (Seat seat : seats) {
                insertSeat.setString(1, seat.getAircraftCode());
                insertSeat.setString(2, seat.getSeatNo());
                insertSeat.setString(3, seat.getFareConditions());
                insertSeat.execute();
            }

        });
    }

    public Set<Seat> getBoardingPasses() throws SQLException {
        return source.statement((stmt) -> {
            Set<Seat> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from seat");

            while(resultSet.next()) {
                result.add(this.createSeat(resultSet));
            }

            return result;
        });
    }
}
