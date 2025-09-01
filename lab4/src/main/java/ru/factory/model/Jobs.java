package ru.factory.model;

public enum Jobs {
    BODY (0),
    MOTORS (1),
    ACCESSORY (2),
    WORKER (3),
    CAR (3),
    DEALER (4);

    final int value;
    Jobs(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
