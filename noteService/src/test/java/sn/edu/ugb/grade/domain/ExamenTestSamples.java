package sn.edu.ugb.grade.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExamenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Examen getExamenSample1() {
        return new Examen().id(1L).typeExamen("typeExamen1").matiereId(1L);
    }

    public static Examen getExamenSample2() {
        return new Examen().id(2L).typeExamen("typeExamen2").matiereId(2L);
    }

    public static Examen getExamenRandomSampleGenerator() {
        return new Examen().id(longCount.incrementAndGet()).typeExamen(UUID.randomUUID().toString()).matiereId(longCount.incrementAndGet());
    }
}
