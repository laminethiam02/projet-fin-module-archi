package sn.edu.ugb.grade.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.grade.domain.Bareme;

/**
 * Spring Data JPA repository for the Bareme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaremeRepository extends JpaRepository<Bareme, Long> {}
