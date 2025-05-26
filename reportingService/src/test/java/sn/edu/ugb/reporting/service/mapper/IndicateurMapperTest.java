package sn.edu.ugb.reporting.service.mapper;

import static sn.edu.ugb.reporting.domain.IndicateurAsserts.*;
import static sn.edu.ugb.reporting.domain.IndicateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndicateurMapperTest {

    private IndicateurMapper indicateurMapper;

    @BeforeEach
    void setUp() {
        indicateurMapper = new IndicateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIndicateurSample1();
        var actual = indicateurMapper.toEntity(indicateurMapper.toDto(expected));
        assertIndicateurAllPropertiesEquals(expected, actual);
    }
}
