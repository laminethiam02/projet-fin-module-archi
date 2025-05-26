package sn.edu.ugb.grade.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.grade.service.dto.BaremeDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.Bareme}.
 */
public interface BaremeService {
    /**
     * Save a bareme.
     *
     * @param baremeDTO the entity to save.
     * @return the persisted entity.
     */
    BaremeDTO save(BaremeDTO baremeDTO);

    /**
     * Updates a bareme.
     *
     * @param baremeDTO the entity to update.
     * @return the persisted entity.
     */
    BaremeDTO update(BaremeDTO baremeDTO);

    /**
     * Partially updates a bareme.
     *
     * @param baremeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BaremeDTO> partialUpdate(BaremeDTO baremeDTO);

    /**
     * Get all the baremes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaremeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bareme.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaremeDTO> findOne(Long id);

    /**
     * Delete the "id" bareme.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
