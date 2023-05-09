package ru.khairullin.ar.domain;

import lombok.Data;

@Data
public class Aircraft {
    private final String aircraftCode;
    private final String model;
    private final int range;
}
