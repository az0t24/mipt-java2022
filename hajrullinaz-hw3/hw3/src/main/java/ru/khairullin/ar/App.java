package ru.khairullin.ar;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ru.khairullin.ar.service.db.DbInit;
import ru.khairullin.ar.service.db.SimpleJdbcTemplate;
import org.h2.jdbcx.JdbcConnectionPool;

import ru.khairullin.ar.service.dao.*;
import ru.khairullin.ar.request.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public final class App {
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(JdbcConnectionPool.create(
            "jdbc:h2:/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/dbcreate.sql;DB_CLOSE_DElAY=-1",
            "admin", ""));
    private static final AircraftDao aircraftDao = new AircraftDao(source);
    private static final AirportDao airportDao = new AirportDao(source);
    private static final BoardingPassDao boardingPassDao = new BoardingPassDao(source);
    private static final BookingDao bookingDao = new BookingDao(source);
    private static final SeatDao seatDao = new SeatDao(source);
    private static final TicketDao ticketDao = new TicketDao(source);

    static void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
    }

    static void tearDownDB() throws SQLException, IOException {
        source.statement((stmt) -> {
            stmt.execute("DROP ALL OBJECTS;");
        });
    }

    private App() {
    }

    public static void unzip(final URL url, final Path decryptTo) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Channels.newInputStream(Channels.newChannel(url.openStream())))) {
            for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
                Path toPath = decryptTo.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(toPath);
                } else try (FileChannel fileChannel = FileChannel.open(toPath, WRITE, CREATE)) {
                    fileChannel.transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
                }
            }
        }
    }

    public static void downloadDB() throws IOException {
        Path DbPath = Path.of("/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/");
        URL DbURL = new URL("https://storage.yandexcloud.net/airtrans-small/airtrans.zip");
        unzip(DbURL, DbPath);
    }

    public static void main(String[] args) throws IOException, SQLException {
//        downloadDB();
//        setupDB();
        request1.doRequest(source);
//        request2.doRequest(source);
//        request3.doRequest(source);
//        request4.doRequest(source);
//        request5.doRequest(source);
//        request6.doRequest(source, "{\"en\": \"Sukhoi Superjet-100\", \"ru\": \"Сухой Суперджет-100\"}");
//        tearDownDB();
    }
}
