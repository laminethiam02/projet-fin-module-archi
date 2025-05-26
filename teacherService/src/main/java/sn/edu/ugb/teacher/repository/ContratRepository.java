package sn.edu.ugb.teacher.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.teacher.domain.Contrat;

/**
 * Spring Data JPA repository for the Contrat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {}
