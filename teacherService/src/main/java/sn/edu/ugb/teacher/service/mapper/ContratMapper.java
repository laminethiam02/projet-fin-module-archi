package sn.edu.ugb.teacher.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.teacher.domain.Contrat;
import sn.edu.ugb.teacher.service.dto.ContratDTO;

/**
 * Mapper for the entity {@link Contrat} and its DTO {@link ContratDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContratMapper extends EntityMapper<ContratDTO, Contrat> {}
