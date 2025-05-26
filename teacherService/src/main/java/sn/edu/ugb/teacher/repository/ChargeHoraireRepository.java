package sn.edu.ugb.teacher.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.teacher.domain.ChargeHoraire;

/**
 * Spring Data JPA repository for the ChargeHoraire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChargeHoraireRepository extends JpaRepository<ChargeHoraire, Long> {}
