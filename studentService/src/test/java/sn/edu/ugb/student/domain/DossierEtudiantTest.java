package sn.edu.ugb.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.student.domain.DossierEtudiantTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.student.web.rest.TestUtil;

class DossierEtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierEtudiant.class);
        DossierEtudiant dossierEtudiant1 = getDossierEtudiantSample1();
        DossierEtudiant dossierEtudiant2 = new DossierEtudiant();
        assertThat(dossierEtudiant1).isNotEqualTo(dossierEtudiant2);

        dossierEtudiant2.setId(dossierEtudiant1.getId());
        assertThat(dossierEtudiant1).isEqualTo(dossierEtudiant2);

        dossierEtudiant2 = getDossierEtudiantSample2();
        assertThat(dossierEtudiant1).isNotEqualTo(dossierEtudiant2);
    }
}
