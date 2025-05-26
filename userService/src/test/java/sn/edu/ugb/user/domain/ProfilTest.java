package sn.edu.ugb.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.edu.ugb.user.domain.ProfilTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.user.web.rest.TestUtil;

class ProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profil.class);
        Profil profil1 = getProfilSample1();
        Profil profil2 = new Profil();
        assertThat(profil1).isNotEqualTo(profil2);

        profil2.setId(profil1.getId());
        assertThat(profil1).isEqualTo(profil2);

        profil2 = getProfilSample2();
        assertThat(profil1).isNotEqualTo(profil2);
    }
}
