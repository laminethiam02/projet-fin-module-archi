package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NiveauTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Niveau getNiveauSample1() {
        return new Niveau().id(1L).libelle("libelle1").ordre(1).programmeId(1L);
    }

    public static Niveau getNiveauSample2() {
        return new Niveau().id(2L).libelle("libelle2").ordre(2).programmeId(2L);
    }

    public static Niveau getNiveauRandomSampleGenerator() {
        return new Niveau()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .ordre(intCount.incrementAndGet())
            .programmeId(longCount.incrementAndGet());
    }
}
