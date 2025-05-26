package sn.edu.ugb.grade.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.grade.domain.Bareme;
import sn.edu.ugb.grade.service.dto.BaremeDTO;

/**
 * Mapper for the entity {@link Bareme} and its DTO {@link BaremeDTO}.
 */
@Mapper(componentModel = "spring")
public interface BaremeMapper extends EntityMapper<BaremeDTO, Bareme> {}
