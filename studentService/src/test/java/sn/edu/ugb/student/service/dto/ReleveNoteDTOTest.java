package sn.edu.ugb.student.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class ReleveNoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleveNoteDTO.class);
        ReleveNoteDTO releveNoteDTO1 = new ReleveNoteDTO();
        releveNoteDTO1.setId(1L);
        ReleveNoteDTO releveNoteDTO2 = new ReleveNoteDTO();
        assertThat(releveNoteDTO1).isNotEqualTo(releveNoteDTO2);
        releveNoteDTO2.setId(releveNoteDTO1.getId());
        assertThat(releveNoteDTO1).isEqualTo(releveNoteDTO2);
        releveNoteDTO2.setId(2L);
        assertThat(releveNoteDTO1).isNotEqualTo(releveNoteDTO2);
        releveNoteDTO1.setId(null);
        assertThat(releveNoteDTO1).isNotEqualTo(releveNoteDTO2);
    }
}
