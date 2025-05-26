package sn.edu.ugb.reporting.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.reporting.domain.RapportStatistique;
import sn.edu.ugb.reporting.service.dto.RapportStatistiqueDTO;

/**
 * Mapper for the entity {@link RapportStatistique} and its DTO {@link RapportStatistiqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface RapportStatistiqueMapper extends EntityMapper<RapportStatistiqueDTO, RapportStatistique> {}
