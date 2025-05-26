package sn.edu.ugb.reporting.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.reporting.web.rest.TestUtil;

class RapportStatistiqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportStatistiqueDTO.class);
        RapportStatistiqueDTO rapportStatistiqueDTO1 = new RapportStatistiqueDTO();
        rapportStatistiqueDTO1.setId(1L);
        RapportStatistiqueDTO rapportStatistiqueDTO2 = new RapportStatistiqueDTO();
        assertThat(rapportStatistiqueDTO1).isNotEqualTo(rapportStatistiqueDTO2);
        rapportStatistiqueDTO2.setId(rapportStatistiqueDTO1.getId());
        assertThat(rapportStatistiqueDTO1).isEqualTo(rapportStatistiqueDTO2);
        rapportStatistiqueDTO2.setId(2L);
        assertThat(rapportStatistiqueDTO1).isNotEqualTo(rapportStatistiqueDTO2);
        rapportStatistiqueDTO1.setId(null);
        assertThat(rapportStatistiqueDTO1).isNotEqualTo(rapportStatistiqueDTO2);
    }
}
