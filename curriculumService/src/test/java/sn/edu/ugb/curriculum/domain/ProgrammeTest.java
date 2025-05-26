package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.ProgrammeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class ProgrammeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Programme.class);
        Programme programme1 = getProgrammeSample1();
        Programme programme2 = new Programme();
        assertThat(programme1).isNotEqualTo(programme2);

        programme2.setId(programme1.getId());
        assertThat(programme1).isEqualTo(programme2);

        programme2 = getProgrammeSample2();
        assertThat(programme1).isNotEqualTo(programme2);
    }
}
