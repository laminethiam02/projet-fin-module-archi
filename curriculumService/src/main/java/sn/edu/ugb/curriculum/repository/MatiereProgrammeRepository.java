package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.MatiereProgramme;

/**
 * Spring Data JPA repository for the MatiereProgramme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatiereProgrammeRepository extends JpaRepository<MatiereProgramme, Long> {}
