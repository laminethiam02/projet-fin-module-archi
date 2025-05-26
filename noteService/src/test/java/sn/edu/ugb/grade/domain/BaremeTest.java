package sn.edu.ugb.grade.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.grade.domain.BaremeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class BaremeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bareme.class);
        Bareme bareme1 = getBaremeSample1();
        Bareme bareme2 = new Bareme();
        assertThat(bareme1).isNotEqualTo(bareme2);

        bareme2.setId(bareme1.getId());
        assertThat(bareme1).isEqualTo(bareme2);

        bareme2 = getBaremeSample2();
        assertThat(bareme1).isNotEqualTo(bareme2);
    }
}
