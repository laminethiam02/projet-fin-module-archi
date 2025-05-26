package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MatiereProgrammeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MatiereProgramme getMatiereProgrammeSample1() {
        return new MatiereProgramme().id(1L).nomMatiere("nomMatiere1").heuresCours(1).ueId(1L);
    }

    public static MatiereProgramme getMatiereProgrammeSample2() {
        return new MatiereProgramme().id(2L).nomMatiere("nomMatiere2").heuresCours(2).ueId(2L);
    }

    public static MatiereProgramme getMatiereProgrammeRandomSampleGenerator() {
        return new MatiereProgramme()
            .id(longCount.incrementAndGet())
            .nomMatiere(UUID.randomUUID().toString())
            .heuresCours(intCount.incrementAndGet())
            .ueId(longCount.incrementAndGet());
    }
}
