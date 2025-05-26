package sn.edu.ugb.grade.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.grade.domain.ExamenTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class ExamenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Examen.class);
        Examen examen1 = getExamenSample1();
        Examen examen2 = new Examen();
        assertThat(examen1).isNotEqualTo(examen2);

        examen2.setId(examen1.getId());
        assertThat(examen1).isEqualTo(examen2);

        examen2 = getExamenSample2();
        assertThat(examen1).isNotEqualTo(examen2);
    }
}
