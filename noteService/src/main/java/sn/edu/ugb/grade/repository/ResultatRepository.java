package sn.edu.ugb.grade.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.grade.domain.Resultat;

/**
 * Spring Data JPA repository for the Resultat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultatRepository extends JpaRepository<Resultat, Long> {}
