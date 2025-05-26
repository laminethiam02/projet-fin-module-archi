package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Programme;
import sn.edu.ugb.curriculum.service.dto.ProgrammeDTO;

/**
 * Mapper for the entity {@link Programme} and its DTO {@link ProgrammeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgrammeMapper extends EntityMapper<ProgrammeDTO, Programme> {}
