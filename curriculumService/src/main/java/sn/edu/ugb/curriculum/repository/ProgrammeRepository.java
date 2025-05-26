package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.Programme;

/**
 * Spring Data JPA repository for the Programme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {}
