package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.DossierEtudiantAsserts.*;
import static sn.edu.ugb.student.domain.DossierEtudiantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DossierEtudiantMapperTest {

    private DossierEtudiantMapper dossierEtudiantMapper;

    @BeforeEach
    void setUp() {
        dossierEtudiantMapper = new DossierEtudiantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDossierEtudiantSample1();
        var actual = dossierEtudiantMapper.toEntity(dossierEtudiantMapper.toDto(expected));
        assertDossierEtudiantAllPropertiesEquals(expected, actual);
    }
}
