package sn.edu.ugb.user.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.user.domain.Profil;
import sn.edu.ugb.user.service.dto.ProfilDTO;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {}
