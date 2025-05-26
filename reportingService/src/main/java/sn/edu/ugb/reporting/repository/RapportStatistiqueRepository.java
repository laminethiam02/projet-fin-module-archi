package sn.edu.ugb.reporting.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.reporting.domain.RapportStatistique;

/**
 * Spring Data JPA repository for the RapportStatistique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportStatistiqueRepository extends JpaRepository<RapportStatistique, Long> {}
