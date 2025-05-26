package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.ReleveNoteAsserts.*;
import static sn.edu.ugb.student.domain.ReleveNoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReleveNoteMapperTest {

    private ReleveNoteMapper releveNoteMapper;

    @BeforeEach
    void setUp() {
        releveNoteMapper = new ReleveNoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReleveNoteSample1();
        var actual = releveNoteMapper.toEntity(releveNoteMapper.toDto(expected));
        assertReleveNoteAllPropertiesEquals(expected, actual);
    }
}
