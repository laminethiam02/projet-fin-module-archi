package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.MatiereProgrammeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class MatiereProgrammeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatiereProgramme.class);
        MatiereProgramme matiereProgramme1 = getMatiereProgrammeSample1();
        MatiereProgramme matiereProgramme2 = new MatiereProgramme();
        assertThat(matiereProgramme1).isNotEqualTo(matiereProgramme2);

        matiereProgramme2.setId(matiereProgramme1.getId());
        assertThat(matiereProgramme1).isEqualTo(matiereProgramme2);

        matiereProgramme2 = getMatiereProgrammeSample2();
        assertThat(matiereProgramme1).isNotEqualTo(matiereProgramme2);
    }
}
