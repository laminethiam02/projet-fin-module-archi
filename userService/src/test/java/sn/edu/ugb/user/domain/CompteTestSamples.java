package sn.edu.ugb.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Compte getCompteSample1() {
        return new Compte().id(1L).login("login1").motDePasse("motDePasse1");
    }

    public static Compte getCompteSample2() {
        return new Compte().id(2L).login("login2").motDePasse("motDePasse2");
    }

    public static Compte getCompteRandomSampleGenerator() {
        return new Compte().id(longCount.incrementAndGet()).login(UUID.randomUUID().toString()).motDePasse(UUID.randomUUID().toString());
    }
}
