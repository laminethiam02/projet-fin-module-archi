package sn.edu.ugb.reporting.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AccesRapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccesRapport getAccesRapportSample1() {
        return new AccesRapport().id(1L).utilisateurId(1L).rapportId(1L);
    }

    public static AccesRapport getAccesRapportSample2() {
        return new AccesRapport().id(2L).utilisateurId(2L).rapportId(2L);
    }

    public static AccesRapport getAccesRapportRandomSampleGenerator() {
        return new AccesRapport()
            .id(longCount.incrementAndGet())
            .utilisateurId(longCount.incrementAndGet())
            .rapportId(longCount.incrementAndGet());
    }
}
