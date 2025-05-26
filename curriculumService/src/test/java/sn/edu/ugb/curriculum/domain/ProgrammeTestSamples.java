package sn.edu.ugb.curriculum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProgrammeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Programme getProgrammeSample1() {
        return new Programme().id(1L).intitule("intitule1").codeProgramme("codeProgramme1");
    }

    public static Programme getProgrammeSample2() {
        return new Programme().id(2L).intitule("intitule2").codeProgramme("codeProgramme2");
    }

    public static Programme getProgrammeRandomSampleGenerator() {
        return new Programme()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .codeProgramme(UUID.randomUUID().toString());
    }
}
