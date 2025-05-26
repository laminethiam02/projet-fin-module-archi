package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BulletinTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Bulletin getBulletinSample1() {
        return new Bulletin().id(1L).anneeAcademique("anneeAcademique1").mention("mention1").dossierId(1L);
    }

    public static Bulletin getBulletinSample2() {
        return new Bulletin().id(2L).anneeAcademique("anneeAcademique2").mention("mention2").dossierId(2L);
    }

    public static Bulletin getBulletinRandomSampleGenerator() {
        return new Bulletin()
            .id(longCount.incrementAndGet())
            .anneeAcademique(UUID.randomUUID().toString())
            .mention(UUID.randomUUID().toString())
            .dossierId(longCount.incrementAndGet());
    }
}
