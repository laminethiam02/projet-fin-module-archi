package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.Niveau;
import sn.edu.ugb.curriculum.service.dto.NiveauDTO;

/**
 * Mapper for the entity {@link Niveau} and its DTO {@link NiveauDTO}.
 */
@Mapper(componentModel = "spring")
public interface NiveauMapper extends EntityMapper<NiveauDTO, Niveau> {}
