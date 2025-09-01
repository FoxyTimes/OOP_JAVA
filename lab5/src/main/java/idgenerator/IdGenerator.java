package idgenerator;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    //генератор ID
    private static final AtomicInteger counter = new AtomicInteger(1);
    public static long newId() {
        return counter.getAndIncrement();
    }
}
