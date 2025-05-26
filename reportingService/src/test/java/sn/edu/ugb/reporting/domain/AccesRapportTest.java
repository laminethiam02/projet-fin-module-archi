package sn.edu.ugb.reporting.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.reporting.domain.AccesRapportTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class AccesRapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccesRapport.class);
        AccesRapport accesRapport1 = getAccesRapportSample1();
        AccesRapport accesRapport2 = new AccesRapport();
        assertThat(accesRapport1).isNotEqualTo(accesRapport2);

        accesRapport2.setId(accesRapport1.getId());
        assertThat(accesRapport1).isEqualTo(accesRapport2);

        accesRapport2 = getAccesRapportSample2();
        assertThat(accesRapport1).isNotEqualTo(accesRapport2);
    }
}
