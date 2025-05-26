package sn.edu.ugb.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndicateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Indicateur getIndicateurSample1() {
        return new Indicateur().id(1L).nom("nom1");
    }

    public static Indicateur getIndicateurSample2() {
        return new Indicateur().id(2L).nom("nom2");
    }

    public static Indicateur getIndicateurRandomSampleGenerator() {
        return new Indicateur().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
