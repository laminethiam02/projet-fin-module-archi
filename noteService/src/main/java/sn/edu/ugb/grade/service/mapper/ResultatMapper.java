package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.Resultat;
import sn.edu.ugb.grade.service.dto.ResultatDTO;

/**
 * Mapper for the entity {@link Resultat} and its DTO {@link ResultatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResultatMapper extends EntityMapper<ResultatDTO, Resultat> {}
