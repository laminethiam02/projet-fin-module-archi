package sn.edu.ugb.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.student.domain.BulletinTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class BulletinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bulletin.class);
        Bulletin bulletin1 = getBulletinSample1();
        Bulletin bulletin2 = new Bulletin();
        assertThat(bulletin1).isNotEqualTo(bulletin2);

        bulletin2.setId(bulletin1.getId());
        assertThat(bulletin1).isEqualTo(bulletin2);

        bulletin2 = getBulletinSample2();
        assertThat(bulletin1).isNotEqualTo(bulletin2);
    }
}
