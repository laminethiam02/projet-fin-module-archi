package sn.edu.ugb.reporting.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.reporting.domain.RapportStatistiqueTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class RapportStatistiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportStatistique.class);
        RapportStatistique rapportStatistique1 = getRapportStatistiqueSample1();
        RapportStatistique rapportStatistique2 = new RapportStatistique();
        assertThat(rapportStatistique1).isNotEqualTo(rapportStatistique2);

        rapportStatistique2.setId(rapportStatistique1.getId());
        assertThat(rapportStatistique1).isEqualTo(rapportStatistique2);

        rapportStatistique2 = getRapportStatistiqueSample2();
        assertThat(rapportStatistique1).isNotEqualTo(rapportStatistique2);
    }
}
