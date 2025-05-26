package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.NiveauAsserts.*;
import static sn.edu.ugb.curriculum.domain.NiveauTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NiveauMapperTest {

    private NiveauMapper niveauMapper;

    @BeforeEach
    void setUp() {
        niveauMapper = new NiveauMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNiveauSample1();
        var actual = niveauMapper.toEntity(niveauMapper.toDto(expected));
        assertNiveauAllPropertiesEquals(expected, actual);
    }
}
