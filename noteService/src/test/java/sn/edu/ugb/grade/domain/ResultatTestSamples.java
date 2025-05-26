package sn.edu.ugb.grade.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResultatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Resultat getResultatSample1() {
        return new Resultat().id(1L).commentaire("commentaire1").examenId(1L).dossierId(1L);
    }

    public static Resultat getResultatSample2() {
        return new Resultat().id(2L).commentaire("commentaire2").examenId(2L).dossierId(2L);
    }

    public static Resultat getResultatRandomSampleGenerator() {
        return new Resultat()
            .id(longCount.incrementAndGet())
            .commentaire(UUID.randomUUID().toString())
            .examenId(longCount.incrementAndGet())
            .dossierId(longCount.incrementAndGet());
    }
}
