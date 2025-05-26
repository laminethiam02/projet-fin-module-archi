package sn.edu.ugb.curriculum.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.curriculum.domain.UE;

/**
 * Spring Data JPA repository for the UE entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UERepository extends JpaRepository<UE, Long> {}
