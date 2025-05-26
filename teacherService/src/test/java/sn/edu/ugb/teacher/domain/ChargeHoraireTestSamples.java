package sn.edu.ugb.teacher.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ChargeHoraireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ChargeHoraire getChargeHoraireSample1() {
        return new ChargeHoraire().id(1L).nombreHeures(1).contratId(1L).classeId(1L);
    }

    public static ChargeHoraire getChargeHoraireSample2() {
        return new ChargeHoraire().id(2L).nombreHeures(2).contratId(2L).classeId(2L);
    }

    public static ChargeHoraire getChargeHoraireRandomSampleGenerator() {
        return new ChargeHoraire()
            .id(longCount.incrementAndGet())
            .nombreHeures(intCount.incrementAndGet())
            .contratId(longCount.incrementAndGet())
            .classeId(longCount.incrementAndGet());
    }
}
