package sn.edu.ugb.grade.service.mapper;

import static sn.edu.ugb.grade.domain.ExamenAsserts.*;
import static sn.edu.ugb.grade.domain.ExamenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExamenMapperTest {

    private ExamenMapper examenMapper;

    @BeforeEach
    void setUp() {
        examenMapper = new ExamenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExamenSample1();
        var actual = examenMapper.toEntity(examenMapper.toDto(expected));
        assertExamenAllPropertiesEquals(expected, actual);
    }
}
