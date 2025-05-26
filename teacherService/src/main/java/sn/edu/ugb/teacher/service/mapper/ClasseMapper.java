package sn.edu.ugb.teacher.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.teacher.domain.Classe;
import sn.edu.ugb.teacher.service.dto.ClasseDTO;

/**
 * Mapper for the entity {@link Classe} and its DTO {@link ClasseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClasseMapper extends EntityMapper<ClasseDTO, Classe> {}
