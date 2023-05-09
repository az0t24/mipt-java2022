package ru.khairullin.ar.service.dao;

import ru.khairullin.ar.domain.Ticket;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TicketDao {
    private final SimpleJdbcTemplate source;

    private Ticket createTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"),
                resultSet.getString("passenger_name"),
                resultSet.getString("contact_data")
        );
    }

    public void saveTickets(Collection<Ticket> tickets) throws SQLException {
        this.source.preparedStatement("insert into seat(ticket_no, " +
                "book_ref, passenger_id, passenger_name, contact_data) values (?, ?, ?, ?, ?)", (insertTicket) -> {
            for (Ticket ticket : tickets) {
                insertTicket.setString(1, ticket.getTicketNo());
                insertTicket.setString(2, ticket.getBookRef());
                insertTicket.setString(3, ticket.getPassengerId());
                insertTicket.setString(4, ticket.getPassengerName());
                insertTicket.setString(5, ticket.getContactData());
                insertTicket.execute();
            }

        });
    }

    public Set<Ticket> getTickets() throws SQLException {
        return source.statement((stmt) -> {
            Set<Ticket> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from ticket");

            while(resultSet.next()) {
                result.add(this.createTicket(resultSet));
            }

            return result;
        });
    }
}
