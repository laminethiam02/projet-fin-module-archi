package sn.edu.ugb.teacher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.teacher.domain.ChargeHoraireTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class ChargeHoraireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChargeHoraire.class);
        ChargeHoraire chargeHoraire1 = getChargeHoraireSample1();
        ChargeHoraire chargeHoraire2 = new ChargeHoraire();
        assertThat(chargeHoraire1).isNotEqualTo(chargeHoraire2);

        chargeHoraire2.setId(chargeHoraire1.getId());
        assertThat(chargeHoraire1).isEqualTo(chargeHoraire2);

        chargeHoraire2 = getChargeHoraireSample2();
        assertThat(chargeHoraire1).isNotEqualTo(chargeHoraire2);
    }
}
