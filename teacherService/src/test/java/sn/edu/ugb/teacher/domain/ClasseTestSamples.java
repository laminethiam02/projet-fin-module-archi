package sn.edu.ugb.teacher.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClasseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Classe getClasseSample1() {
        return new Classe().id(1L).nomClasse("nomClasse1").anneeScolaire("anneeScolaire1");
    }

    public static Classe getClasseSample2() {
        return new Classe().id(2L).nomClasse("nomClasse2").anneeScolaire("anneeScolaire2");
    }

    public static Classe getClasseRandomSampleGenerator() {
        return new Classe()
            .id(longCount.incrementAndGet())
            .nomClasse(UUID.randomUUID().toString())
            .anneeScolaire(UUID.randomUUID().toString());
    }
}
