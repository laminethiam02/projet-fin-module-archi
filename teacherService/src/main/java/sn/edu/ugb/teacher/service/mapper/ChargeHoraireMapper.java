package sn.edu.ugb.teacher.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.teacher.domain.ChargeHoraire;
import sn.edu.ugb.teacher.service.dto.ChargeHoraireDTO;

/**
 * Mapper for the entity {@link ChargeHoraire} and its DTO {@link ChargeHoraireDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChargeHoraireMapper extends EntityMapper<ChargeHoraireDTO, ChargeHoraire> {}
