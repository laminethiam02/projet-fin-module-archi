package sn.edu.ugb.curriculum.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.curriculum.domain.UE;
import sn.edu.ugb.curriculum.service.dto.UEDTO;

/**
 * Mapper for the entity {@link UE} and its DTO {@link UEDTO}.
 */
@Mapper(componentModel = "spring")
public interface UEMapper extends EntityMapper<UEDTO, UE> {}
