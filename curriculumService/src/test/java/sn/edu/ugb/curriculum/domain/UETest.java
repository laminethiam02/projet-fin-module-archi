package sn.edu.ugb.curriculum.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.curriculum.domain.UETestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class UETest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UE.class);
        UE uE1 = getUESample1();
        UE uE2 = new UE();
        assertThat(uE1).isNotEqualTo(uE2);

        uE2.setId(uE1.getId());
        assertThat(uE1).isEqualTo(uE2);

        uE2 = getUESample2();
        assertThat(uE1).isNotEqualTo(uE2);
    }
}
