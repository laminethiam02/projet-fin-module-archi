package sn.edu.ugb.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RapportStatistiqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RapportStatistique getRapportStatistiqueSample1() {
        return new RapportStatistique().id(1L).titre("titre1").description("description1").indicateurId(1L);
    }

    public static RapportStatistique getRapportStatistiqueSample2() {
        return new RapportStatistique().id(2L).titre("titre2").description("description2").indicateurId(2L);
    }

    public static RapportStatistique getRapportStatistiqueRandomSampleGenerator() {
        return new RapportStatistique()
            .id(longCount.incrementAndGet())
            .titre(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .indicateurId(longCount.incrementAndGet());
    }
}
