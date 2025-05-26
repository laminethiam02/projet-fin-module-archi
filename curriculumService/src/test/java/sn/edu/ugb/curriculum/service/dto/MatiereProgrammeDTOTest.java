package sn.edu.ugb.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class MatiereProgrammeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatiereProgrammeDTO.class);
        MatiereProgrammeDTO matiereProgrammeDTO1 = new MatiereProgrammeDTO();
        matiereProgrammeDTO1.setId(1L);
        MatiereProgrammeDTO matiereProgrammeDTO2 = new MatiereProgrammeDTO();
        assertThat(matiereProgrammeDTO1).isNotEqualTo(matiereProgrammeDTO2);
        matiereProgrammeDTO2.setId(matiereProgrammeDTO1.getId());
        assertThat(matiereProgrammeDTO1).isEqualTo(matiereProgrammeDTO2);
        matiereProgrammeDTO2.setId(2L);
        assertThat(matiereProgrammeDTO1).isNotEqualTo(matiereProgrammeDTO2);
        matiereProgrammeDTO1.setId(null);
        assertThat(matiereProgrammeDTO1).isNotEqualTo(matiereProgrammeDTO2);
    }
}
