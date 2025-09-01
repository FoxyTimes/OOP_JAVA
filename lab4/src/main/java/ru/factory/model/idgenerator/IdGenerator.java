package ru.factory.model.idgenerator;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    //генератор ID
    private static final AtomicInteger counter = new AtomicInteger(0);
    public static long newId() {
        return counter.getAndIncrement();
    }
}
