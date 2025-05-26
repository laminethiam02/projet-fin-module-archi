package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.Bulletin;
import sn.edu.ugb.student.service.dto.BulletinDTO;

/**
 * Mapper for the entity {@link Bulletin} and its DTO {@link BulletinDTO}.
 */
@Mapper(componentModel = "spring")
public interface BulletinMapper extends EntityMapper<BulletinDTO, Bulletin> {}
