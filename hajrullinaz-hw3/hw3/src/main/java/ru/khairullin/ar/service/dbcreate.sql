CREATE TABLE IF NOT EXISTS aircraft (
    PRIMARY KEY (aircraft_code),
    aircraft_code CHAR(3)      NOT NULL,
    model         VARCHAR(255) NOT NULL,
    range         INTEGER      NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/aircrafts_data.csv',
                           'aircraft_code, model, range');

CREATE TABLE IF NOT EXISTS airport (
    PRIMARY KEY (airport_code),
    airport_code CHAR(3)      NOT NULL,
    airport_name VARCHAR(255) NOT NULL,
    city         VARCHAR(255) NOT NULL,
    coordinates  VARCHAR(255) NOT NULL,
    timezone     TEXT         NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/airports_data.csv',
                           'airport_code, airport_name, city, coordinates, timezone');

CREATE TABLE IF NOT EXISTS flight (
    PRIMARY KEY (flight_no, scheduled_departure),
    flight_id           INTEGER     NOT NULL,
    flight_no           CHAR(6)     NOT NULL,
    scheduled_departure TIMESTAMP   NOT NULL,
    scheduled_arrival   TIMESTAMP   NOT NULL,
    departure_airport   CHAR(3)     NOT NULL,
    arrival_airport     CHAR(3)     NOT NULL,
    status              VARCHAR(20) NOT NULL,
    aircraft_code       CHAR(3)     NOT NULL,
    actual_departure    TIMESTAMP,
    actual_arrival      TIMESTAMP
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/flights.csv',
                           'flight_id,flight_no,scheduled_departure, scheduled_arrival, departure_airport,
                       --   arrival_airport, status, aircraft_code, actual_departure, actual_arrival');

CREATE TABLE IF NOT EXISTS ticket (
    PRIMARY KEY (ticket_no),
    ticket_no      CHAR(13)     NOT NULL,
    book_ref       CHAR(6)      NOT NULL,
    passenger_id   VARCHAR(20)  NOT NULL,
    passenger_name TEXT         NOT NULL,
    contact_data   VARCHAR(255)
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/data/tickets.csv',
                          'ticket_no, book_ref, passenger_id, passenger_name, contact_data');

CREATE TABLE IF NOT EXISTS boarding_pass (
    PRIMARY KEY (ticket_no, flight_id),
    FOREIGN KEY (ticket_no) REFERENCES ticket(ticket_no) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flight(flight_id) ON DELETE CASCADE,
    ticket_no   CHAR(13)   NOT NULL,
    flight_id   INTEGER    NOT NULL,
    boarding_no INTEGER    NOT NULL,
    seat_no     VARCHAR(4) NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/boarding_passes.csv',
                           'ticket_no, flight_id, boarding_no, seat_no');

CREATE TABLE IF NOT EXISTS booking (
    PRIMARY KEY (book_ref),
    book_ref     CHAR(6)        NOT NULL,
    book_date    TIMESTAMP      NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/bookings.csv',
                           'book_ref, book_date, total_amount');

CREATE TABLE IF NOT EXISTS seat (
    PRIMARY KEY (aircraft_code, seat_no),
    FOREIGN KEY (aircraft_code) REFERENCES aircraft(aircraft_code) ON DELETE CASCADE,
    aircraft_code   CHAR(3)     NOT NULL,
    seat_no         VARCHAR(4)  NOT NULL,
    fare_conditions VARCHAR(10) NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/seats.csv',
                           'aircraft_code, seat_no, fare_conditions');

CREATE TABLE IF NOT EXISTS ticket_flight (
    PRIMARY KEY (ticket_no, flight_id),
    FOREIGN KEY (ticket_no) REFERENCES ticket(ticket_no) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flight(flight_id) ON DELETE CASCADE,
    ticket_no       CHAR(13)       NOT NULL,
    flight_id       INTEGER        NOT NULL,
    fare_conditions VARCHAR(10)    NOT NULL,
    amount          NUMERIC(10, 2) NOT NULL
) AS SELECT * FROM csvread('/home/az0t24/mipt-java/hw3/hw3/src/main/resources/ru/khairullin/ar/data/ticket_flights.csv',
                           'ticket_no, flight_id, fare_conditions, amount');