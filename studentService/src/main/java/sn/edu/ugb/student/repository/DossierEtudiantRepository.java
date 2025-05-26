package sn.edu.ugb.student.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.student.domain.DossierEtudiant;

/**
 * Spring Data JPA repository for the DossierEtudiant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierEtudiantRepository extends JpaRepository<DossierEtudiant, Long> {}
