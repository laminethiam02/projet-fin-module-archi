package sn.edu.ugb.grade.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.grade.web.rest.TestUtil;

class BaremeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaremeDTO.class);
        BaremeDTO baremeDTO1 = new BaremeDTO();
        baremeDTO1.setId(1L);
        BaremeDTO baremeDTO2 = new BaremeDTO();
        assertThat(baremeDTO1).isNotEqualTo(baremeDTO2);
        baremeDTO2.setId(baremeDTO1.getId());
        assertThat(baremeDTO1).isEqualTo(baremeDTO2);
        baremeDTO2.setId(2L);
        assertThat(baremeDTO1).isNotEqualTo(baremeDTO2);
        baremeDTO1.setId(null);
        assertThat(baremeDTO1).isNotEqualTo(baremeDTO2);
    }
}
