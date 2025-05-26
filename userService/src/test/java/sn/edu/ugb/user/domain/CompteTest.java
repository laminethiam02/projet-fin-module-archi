package sn.edu.ugb.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.user.domain.CompteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.user.web.rest.TestUtil;

class CompteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compte.class);
        Compte compte1 = getCompteSample1();
        Compte compte2 = new Compte();
        assertThat(compte1).isNotEqualTo(compte2);

        compte2.setId(compte1.getId());
        assertThat(compte1).isEqualTo(compte2);

        compte2 = getCompteSample2();
        assertThat(compte1).isNotEqualTo(compte2);
    }
}
