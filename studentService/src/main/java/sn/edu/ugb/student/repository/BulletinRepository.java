package sn.edu.ugb.student.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.edu.ugb.student.domain.Bulletin;

/**
 * Spring Data JPA repository for the Bulletin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {}
