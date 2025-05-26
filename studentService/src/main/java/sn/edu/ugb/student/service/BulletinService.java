package sn.edu.ugb.student.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.student.service.dto.BulletinDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.student.domain.Bulletin}.
 */
public interface BulletinService {
    /**
     * Save a bulletin.
     *
     * @param bulletinDTO the entity to save.
     * @return the persisted entity.
     */
    BulletinDTO save(BulletinDTO bulletinDTO);

    /**
     * Updates a bulletin.
     *
     * @param bulletinDTO the entity to update.
     * @return the persisted entity.
     */
    BulletinDTO update(BulletinDTO bulletinDTO);

    /**
     * Partially updates a bulletin.
     *
     * @param bulletinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BulletinDTO> partialUpdate(BulletinDTO bulletinDTO);

    /**
     * Get all the bulletins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BulletinDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bulletin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BulletinDTO> findOne(Long id);

    /**
     * Delete the "id" bulletin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
