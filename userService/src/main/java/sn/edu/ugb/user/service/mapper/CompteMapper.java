package sn.edu.ugb.user.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.user.domain.Compte;
import sn.edu.ugb.user.service.dto.CompteDTO;

/**
 * Mapper for the entity {@link Compte} and its DTO {@link CompteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteMapper extends EntityMapper<CompteDTO, Compte> {}
