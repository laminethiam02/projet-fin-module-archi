package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.MatiereProgramme;
import sn.edu.ugb.curriculum.service.dto.MatiereProgrammeDTO;

/**
 * Mapper for the entity {@link MatiereProgramme} and its DTO {@link MatiereProgrammeDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatiereProgrammeMapper extends EntityMapper<MatiereProgrammeDTO, MatiereProgramme> {}
