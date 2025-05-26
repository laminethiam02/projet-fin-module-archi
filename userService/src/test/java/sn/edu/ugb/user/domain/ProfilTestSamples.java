package sn.edu.ugb.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profil getProfilSample1() {
        return new Profil().id(1L).nomComplet("nomComplet1").adresse("adresse1").email("email1").telephone("telephone1").compteId(1L);
    }

    public static Profil getProfilSample2() {
        return new Profil().id(2L).nomComplet("nomComplet2").adresse("adresse2").email("email2").telephone("telephone2").compteId(2L);
    }

    public static Profil getProfilRandomSampleGenerator() {
        return new Profil()
            .id(longCount.incrementAndGet())
            .nomComplet(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .compteId(longCount.incrementAndGet());
    }
}
