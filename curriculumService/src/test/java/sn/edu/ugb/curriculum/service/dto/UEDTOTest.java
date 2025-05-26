package sn.edu.ugb.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class UEDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UEDTO.class);
        UEDTO uEDTO1 = new UEDTO();
        uEDTO1.setId(1L);
        UEDTO uEDTO2 = new UEDTO();
        assertThat(uEDTO1).isNotEqualTo(uEDTO2);
        uEDTO2.setId(uEDTO1.getId());
        assertThat(uEDTO1).isEqualTo(uEDTO2);
        uEDTO2.setId(2L);
        assertThat(uEDTO1).isNotEqualTo(uEDTO2);
        uEDTO1.setId(null);
        assertThat(uEDTO1).isNotEqualTo(uEDTO2);
    }
}
