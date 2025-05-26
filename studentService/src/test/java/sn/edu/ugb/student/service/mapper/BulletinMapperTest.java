package sn.edu.ugb.student.service.mapper;

import static sn.edu.ugb.student.domain.BulletinAsserts.*;
import static sn.edu.ugb.student.domain.BulletinTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BulletinMapperTest {

    private BulletinMapper bulletinMapper;

    @BeforeEach
    void setUp() {
        bulletinMapper = new BulletinMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBulletinSample1();
        var actual = bulletinMapper.toEntity(bulletinMapper.toDto(expected));
        assertBulletinAllPropertiesEquals(expected, actual);
    }
}
