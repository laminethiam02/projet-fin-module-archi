package sn.edu.ugb.teacher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.teacher.domain.ClasseTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class ClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classe.class);
        Classe classe1 = getClasseSample1();
        Classe classe2 = new Classe();
        assertThat(classe1).isNotEqualTo(classe2);

        classe2.setId(classe1.getId());
        assertThat(classe1).isEqualTo(classe2);

        classe2 = getClasseSample2();
        assertThat(classe1).isNotEqualTo(classe2);
    }
}
