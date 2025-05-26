package sn.edu.ugb.reporting.service.mapper;

import static sn.edu.ugb.reporting.domain.RapportStatistiqueAsserts.*;
import static sn.edu.ugb.reporting.domain.RapportStatistiqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RapportStatistiqueMapperTest {

    private RapportStatistiqueMapper rapportStatistiqueMapper;

    @BeforeEach
    void setUp() {
        rapportStatistiqueMapper = new RapportStatistiqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRapportStatistiqueSample1();
        var actual = rapportStatistiqueMapper.toEntity(rapportStatistiqueMapper.toDto(expected));
        assertRapportStatistiqueAllPropertiesEquals(expected, actual);
    }
}
