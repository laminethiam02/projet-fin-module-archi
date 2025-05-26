package sn.edu.ugb.grade.service.mapper;

import static sn.edu.ugb.grade.domain.BaremeAsserts.*;
import static sn.edu.ugb.grade.domain.BaremeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaremeMapperTest {

    private BaremeMapper baremeMapper;

    @BeforeEach
    void setUp() {
        baremeMapper = new BaremeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBaremeSample1();
        var actual = baremeMapper.toEntity(baremeMapper.toDto(expected));
        assertBaremeAllPropertiesEquals(expected, actual);
    }
}
