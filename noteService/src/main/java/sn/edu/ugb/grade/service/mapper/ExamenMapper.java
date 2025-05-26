package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.Examen;
import sn.edu.ugb.grade.service.dto.ExamenDTO;

/**
 * Mapper for the entity {@link Examen} and its DTO {@link ExamenDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamenMapper extends EntityMapper<ExamenDTO, Examen> {}
