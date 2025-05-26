package sn.edu.ugb.teacher.service.mapper;

import static sn.edu.ugb.teacher.domain.ChargeHoraireAsserts.*;
import static sn.edu.ugb.teacher.domain.ChargeHoraireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChargeHoraireMapperTest {

    private ChargeHoraireMapper chargeHoraireMapper;

    @BeforeEach
    void setUp() {
        chargeHoraireMapper = new ChargeHoraireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChargeHoraireSample1();
        var actual = chargeHoraireMapper.toEntity(chargeHoraireMapper.toDto(expected));
        assertChargeHoraireAllPropertiesEquals(expected, actual);
    }
}
