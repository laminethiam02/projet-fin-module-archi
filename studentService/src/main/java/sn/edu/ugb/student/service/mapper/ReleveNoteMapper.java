package sn.edu.ugb.student.service.mapper;

import org.mapstruct.*;
import sn.edu.ugb.student.domain.ReleveNote;
import sn.edu.ugb.student.service.dto.ReleveNoteDTO;

/**
 * Mapper for the entity {@link ReleveNote} and its DTO {@link ReleveNoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReleveNoteMapper extends EntityMapper<ReleveNoteDTO, ReleveNote> {}
