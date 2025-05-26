package sn.edu.ugb.curriculum.service.mapper;

import static sn.edu.ugb.curriculum.domain.ProgrammeAsserts.*;
import static sn.edu.ugb.curriculum.domain.ProgrammeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgrammeMapperTest {

    private ProgrammeMapper programmeMapper;

    @BeforeEach
    void setUp() {
        programmeMapper = new ProgrammeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProgrammeSample1();
        var actual = programmeMapper.toEntity(programmeMapper.toDto(expected));
        assertProgrammeAllPropertiesEquals(expected, actual);
    }
}
