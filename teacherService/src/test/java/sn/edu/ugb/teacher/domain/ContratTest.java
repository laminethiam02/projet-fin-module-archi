package sn.edu.ugb.teacher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.teacher.domain.ContratTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class ContratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contrat.class);
        Contrat contrat1 = getContratSample1();
        Contrat contrat2 = new Contrat();
        assertThat(contrat1).isNotEqualTo(contrat2);

        contrat2.setId(contrat1.getId());
        assertThat(contrat1).isEqualTo(contrat2);

        contrat2 = getContratSample2();
        assertThat(contrat1).isNotEqualTo(contrat2);
    }
}
