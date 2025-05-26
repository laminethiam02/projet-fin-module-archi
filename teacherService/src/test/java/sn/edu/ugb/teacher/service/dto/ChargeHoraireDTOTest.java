package sn.edu.ugb.teacher.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.edu.ugb.teacher.web.rest.TestUtil;

class ChargeHoraireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChargeHoraireDTO.class);
        ChargeHoraireDTO chargeHoraireDTO1 = new ChargeHoraireDTO();
        chargeHoraireDTO1.setId(1L);
        ChargeHoraireDTO chargeHoraireDTO2 = new ChargeHoraireDTO();
        assertThat(chargeHoraireDTO1).isNotEqualTo(chargeHoraireDTO2);
        chargeHoraireDTO2.setId(chargeHoraireDTO1.getId());
        assertThat(chargeHoraireDTO1).isEqualTo(chargeHoraireDTO2);
        chargeHoraireDTO2.setId(2L);
        assertThat(chargeHoraireDTO1).isNotEqualTo(chargeHoraireDTO2);
        chargeHoraireDTO1.setId(null);
        assertThat(chargeHoraireDTO1).isNotEqualTo(chargeHoraireDTO2);
    }
}
