package sn.edu.ugb.student.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class DossierEtudiantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierEtudiantDTO.class);
        DossierEtudiantDTO dossierEtudiantDTO1 = new DossierEtudiantDTO();
        dossierEtudiantDTO1.setId(1L);
        DossierEtudiantDTO dossierEtudiantDTO2 = new DossierEtudiantDTO();
        assertThat(dossierEtudiantDTO1).isNotEqualTo(dossierEtudiantDTO2);
        dossierEtudiantDTO2.setId(dossierEtudiantDTO1.getId());
        assertThat(dossierEtudiantDTO1).isEqualTo(dossierEtudiantDTO2);
        dossierEtudiantDTO2.setId(2L);
        assertThat(dossierEtudiantDTO1).isNotEqualTo(dossierEtudiantDTO2);
        dossierEtudiantDTO1.setId(null);
        assertThat(dossierEtudiantDTO1).isNotEqualTo(dossierEtudiantDTO2);
    }
}
