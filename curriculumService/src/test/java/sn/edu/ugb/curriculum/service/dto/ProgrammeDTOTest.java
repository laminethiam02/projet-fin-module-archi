package sn.edu.ugb.curriculum.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.curriculum.web.rest.TestUtil;

class ProgrammeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgrammeDTO.class);
        ProgrammeDTO programmeDTO1 = new ProgrammeDTO();
        programmeDTO1.setId(1L);
        ProgrammeDTO programmeDTO2 = new ProgrammeDTO();
        assertThat(programmeDTO1).isNotEqualTo(programmeDTO2);
        programmeDTO2.setId(programmeDTO1.getId());
        assertThat(programmeDTO1).isEqualTo(programmeDTO2);
        programmeDTO2.setId(2L);
        assertThat(programmeDTO1).isNotEqualTo(programmeDTO2);
        programmeDTO1.setId(null);
        assertThat(programmeDTO1).isNotEqualTo(programmeDTO2);
    }
}
