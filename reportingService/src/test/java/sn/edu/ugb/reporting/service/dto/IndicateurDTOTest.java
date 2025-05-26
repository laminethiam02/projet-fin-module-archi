package sn.edu.ugb.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class IndicateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndicateurDTO.class);
        IndicateurDTO indicateurDTO1 = new IndicateurDTO();
        indicateurDTO1.setId(1L);
        IndicateurDTO indicateurDTO2 = new IndicateurDTO();
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
        indicateurDTO2.setId(indicateurDTO1.getId());
        assertThat(indicateurDTO1).isEqualTo(indicateurDTO2);
        indicateurDTO2.setId(2L);
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
        indicateurDTO1.setId(null);
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
    }
}
