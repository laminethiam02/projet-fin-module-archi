package sn.edu.ugb.teacher.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContratTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contrat getContratSample1() {
        return new Contrat().id(1L).typeContrat("typeContrat1").profilId(1L);
    }

    public static Contrat getContratSample2() {
        return new Contrat().id(2L).typeContrat("typeContrat2").profilId(2L);
    }

    public static Contrat getContratRandomSampleGenerator() {
        return new Contrat()
            .id(longCount.incrementAndGet())
            .typeContrat(UUID.randomUUID().toString())
            .profilId(longCount.incrementAndGet());
    }
}
