package sn.edu.ugb.grade.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.grade.domain.ResultatTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class ResultatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resultat.class);
        Resultat resultat1 = getResultatSample1();
        Resultat resultat2 = new Resultat();
        assertThat(resultat1).isNotEqualTo(resultat2);

        resultat2.setId(resultat1.getId());
        assertThat(resultat1).isEqualTo(resultat2);

        resultat2 = getResultatSample2();
        assertThat(resultat1).isNotEqualTo(resultat2);
    }
}
