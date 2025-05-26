package sn.edu.ugb.reporting.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.reporting.domain.IndicateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class IndicateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Indicateur.class);
        Indicateur indicateur1 = getIndicateurSample1();
        Indicateur indicateur2 = new Indicateur();
        assertThat(indicateur1).isNotEqualTo(indicateur2);

        indicateur2.setId(indicateur1.getId());
        assertThat(indicateur1).isEqualTo(indicateur2);

        indicateur2 = getIndicateurSample2();
        assertThat(indicateur1).isNotEqualTo(indicateur2);
    }
}
