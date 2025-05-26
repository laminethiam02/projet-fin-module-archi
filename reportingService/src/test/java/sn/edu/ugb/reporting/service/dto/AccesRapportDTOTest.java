package sn.edu.ugb.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class AccesRapportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccesRapportDTO.class);
        AccesRapportDTO accesRapportDTO1 = new AccesRapportDTO();
        accesRapportDTO1.setId(1L);
        AccesRapportDTO accesRapportDTO2 = new AccesRapportDTO();
        assertThat(accesRapportDTO1).isNotEqualTo(accesRapportDTO2);
        accesRapportDTO2.setId(accesRapportDTO1.getId());
        assertThat(accesRapportDTO1).isEqualTo(accesRapportDTO2);
        accesRapportDTO2.setId(2L);
        assertThat(accesRapportDTO1).isNotEqualTo(accesRapportDTO2);
        accesRapportDTO1.setId(null);
        assertThat(accesRapportDTO1).isNotEqualTo(accesRapportDTO2);
    }
}
