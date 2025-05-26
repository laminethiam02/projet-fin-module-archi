package sn.edu.ugb.reporting.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.reporting.domain.AccesRapport;
import sn.edu.ugb.reporting.service.dto.AccesRapportDTO;

/**
 * Mapper for the entity {@link AccesRapport} and its DTO {@link AccesRapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccesRapportMapper extends EntityMapper<AccesRapportDTO, AccesRapport> {}
