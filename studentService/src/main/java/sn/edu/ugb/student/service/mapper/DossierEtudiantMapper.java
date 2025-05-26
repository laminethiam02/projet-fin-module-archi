package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.DossierEtudiant;
import sn.edu.ugb.student.service.dto.DossierEtudiantDTO;

/**
 * Mapper for the entity {@link DossierEtudiant} and its DTO {@link DossierEtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierEtudiantMapper extends EntityMapper<DossierEtudiantDTO, DossierEtudiant> {}
