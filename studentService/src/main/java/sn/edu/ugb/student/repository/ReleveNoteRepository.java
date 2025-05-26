package sn.edu.ugb.student.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.student.domain.ReleveNote;

/**
 * Spring Data JPA repository for the ReleveNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleveNoteRepository extends JpaRepository<ReleveNote, Long> {}
