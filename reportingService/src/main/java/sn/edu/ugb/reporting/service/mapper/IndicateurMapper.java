package sn.edu.ugb.reporting.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.reporting.domain.Indicateur;
import sn.edu.ugb.reporting.service.dto.IndicateurDTO;

/**
 * Mapper for the entity {@link Indicateur} and its DTO {@link IndicateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface IndicateurMapper extends EntityMapper<IndicateurDTO, Indicateur> {}
