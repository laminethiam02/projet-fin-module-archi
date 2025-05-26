package sn.edu.ugb.reporting.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.reporting.domain.Indicateur;

/**
 * Spring Data JPA repository for the Indicateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicateurRepository extends JpaRepository<Indicateur, Long> {}
