package sn.edu.ugb.grade.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.grade.domain.Examen;

/**
 * Spring Data JPA repository for the Examen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {}
