package sn.edu.ugb.reporting.service.mapper;

import static sn.edu.ugb.reporting.domain.AccesRapportAsserts.*;
import static sn.edu.ugb.reporting.domain.AccesRapportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccesRapportMapperTest {

    private AccesRapportMapper accesRapportMapper;

    @BeforeEach
    void setUp() {
        accesRapportMapper = new AccesRapportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccesRapportSample1();
        var actual = accesRapportMapper.toEntity(accesRapportMapper.toDto(expected));
        assertAccesRapportAllPropertiesEquals(expected, actual);
    }
}
