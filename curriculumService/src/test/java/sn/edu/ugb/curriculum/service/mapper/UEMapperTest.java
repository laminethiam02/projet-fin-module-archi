package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.UEAsserts.*;
import static sn.edu.ugb.curriculum.domain.UETestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UEMapperTest {

    private UEMapper uEMapper;

    @BeforeEach
    void setUp() {
        uEMapper = new UEMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUESample1();
        var actual = uEMapper.toEntity(uEMapper.toDto(expected));
        assertUEAllPropertiesEquals(expected, actual);
    }
}
