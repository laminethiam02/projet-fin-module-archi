package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.UEDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.UE}.
 */
public interface UEService {
    /**
     * Save a uE.
     *
     * @param uEDTO the entity to save.
     * @return the persisted entity.
     */
    UEDTO save(UEDTO uEDTO);

    /**
     * Updates a uE.
     *
     * @param uEDTO the entity to update.
     * @return the persisted entity.
     */
    UEDTO update(UEDTO uEDTO);

    /**
     * Partially updates a uE.
     *
     * @param uEDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UEDTO> partialUpdate(UEDTO uEDTO);

    /**
     * Get all the uES.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UEDTO> findAll(Pageable pageable);

    /**
     * Get the "id" uE.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UEDTO> findOne(Long id);

    /**
     * Delete the "id" uE.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
