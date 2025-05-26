package sn.edu.ugb.grade.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaremeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Bareme getBaremeSample1() {
        return new Bareme().id(1L).intitule("intitule1").totalPoints(1).examenId(1L);
    }

    public static Bareme getBaremeSample2() {
        return new Bareme().id(2L).intitule("intitule2").totalPoints(2).examenId(2L);
    }

    public static Bareme getBaremeRandomSampleGenerator() {
        return new Bareme()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .totalPoints(intCount.incrementAndGet())
            .examenId(longCount.incrementAndGet());
    }
}
