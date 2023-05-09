package ru.khairullin.ar.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Booking {
    private final String bookRef;
    private final Timestamp bookDate;
    private final int totalAmount;
}