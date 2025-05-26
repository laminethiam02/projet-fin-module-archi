package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DossierEtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DossierEtudiant getDossierEtudiantSample1() {
        return new DossierEtudiant().id(1L).matricule("matricule1").nationalite("nationalite1").profilId(1L);
    }

    public static DossierEtudiant getDossierEtudiantSample2() {
        return new DossierEtudiant().id(2L).matricule("matricule2").nationalite("nationalite2").profilId(2L);
    }

    public static DossierEtudiant getDossierEtudiantRandomSampleGenerator() {
        return new DossierEtudiant()
            .id(longCount.incrementAndGet())
            .matricule(UUID.randomUUID().toString())
            .nationalite(UUID.randomUUID().toString())
            .profilId(longCount.incrementAndGet());
    }
}
