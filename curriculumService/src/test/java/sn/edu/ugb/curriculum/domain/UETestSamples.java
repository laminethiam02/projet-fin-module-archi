package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UETestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UE getUESample1() {
        return new UE().id(1L).intitule("intitule1").credits(1).niveauId(1L);
    }

    public static UE getUESample2() {
        return new UE().id(2L).intitule("intitule2").credits(2).niveauId(2L);
    }

    public static UE getUERandomSampleGenerator() {
        return new UE()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .credits(intCount.incrementAndGet())
            .niveauId(longCount.incrementAndGet());
    }
}
