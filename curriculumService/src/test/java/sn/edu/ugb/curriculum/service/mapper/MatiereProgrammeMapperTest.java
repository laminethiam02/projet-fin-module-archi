package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.MatiereProgrammeAsserts.*;
import static sn.edu.ugb.curriculum.domain.MatiereProgrammeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatiereProgrammeMapperTest {

    private MatiereProgrammeMapper matiereProgrammeMapper;

    @BeforeEach
    void setUp() {
        matiereProgrammeMapper = new MatiereProgrammeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMatiereProgrammeSample1();
        var actual = matiereProgrammeMapper.toEntity(matiereProgrammeMapper.toDto(expected));
        assertMatiereProgrammeAllPropertiesEquals(expected, actual);
    }
}
