package ru.khairullin.ar.service.dao;

import ru.khairullin.ar.domain.Booking;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class BookingDao {
    private final SimpleJdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException {
        return new Booking(
                resultSet.getString("book_ref"),
                resultSet.getTimestamp("book_date"),
                resultSet.getInt("total_amount")
        );
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException {
        this.source.preparedStatement("insert into booking(book_red, " +
                "book_date, total_amount) values (?, ?, ?)", (insertBooking) -> {
            for (Booking booking : bookings) {
                insertBooking.setString(1, booking.getBookRef());
                insertBooking.setObject(2, booking.getBookDate());
                insertBooking.setInt(3, booking.getTotalAmount());
                insertBooking.execute();
            }
        });
    }

    public Set<Booking> getBookings() throws SQLException {
        return source.statement((stmt) -> {
            Set<Booking> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from booking");

            while(resultSet.next()) {
                result.add(this.createBooking(resultSet));
            }

            return result;
        });
    }
}
