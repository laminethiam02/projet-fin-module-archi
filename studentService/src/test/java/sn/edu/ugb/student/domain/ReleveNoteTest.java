package sn.edu.ugb.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.student.domain.ReleveNoteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class ReleveNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleveNote.class);
        ReleveNote releveNote1 = getReleveNoteSample1();
        ReleveNote releveNote2 = new ReleveNote();
        assertThat(releveNote1).isNotEqualTo(releveNote2);

        releveNote2.setId(releveNote1.getId());
        assertThat(releveNote1).isEqualTo(releveNote2);

        releveNote2 = getReleveNoteSample2();
        assertThat(releveNote1).isNotEqualTo(releveNote2);
    }
}
