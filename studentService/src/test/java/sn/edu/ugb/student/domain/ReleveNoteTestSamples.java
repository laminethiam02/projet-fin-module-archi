package sn.edu.ugb.student.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReleveNoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReleveNote getReleveNoteSample1() {
        return new ReleveNote().id(1L).semestre("semestre1").dossierId(1L);
    }

    public static ReleveNote getReleveNoteSample2() {
        return new ReleveNote().id(2L).semestre("semestre2").dossierId(2L);
    }

    public static ReleveNote getReleveNoteRandomSampleGenerator() {
        return new ReleveNote()
            .id(longCount.incrementAndGet())
            .semestre(UUID.randomUUID().toString())
            .dossierId(longCount.incrementAndGet());
    }
}
