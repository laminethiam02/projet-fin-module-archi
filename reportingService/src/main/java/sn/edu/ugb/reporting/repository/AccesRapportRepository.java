package sn.edu.ugb.reporting.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.reporting.domain.AccesRapport;

/**
 * Spring Data JPA repository for the AccesRapport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccesRapportRepository extends JpaRepository<AccesRapport, Long> {}
