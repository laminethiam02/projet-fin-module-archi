package sn.edu.ugb.grade.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.grade.service.dto.ExamenDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.Examen}.
 */
public interface ExamenService {
    /**
     * Save a examen.
     *
     * @param examenDTO the entity to save.
     * @return the persisted entity.
     */
    ExamenDTO save(ExamenDTO examenDTO);

    /**
     * Updates a examen.
     *
     * @param examenDTO the entity to update.
     * @return the persisted entity.
     */
    ExamenDTO update(ExamenDTO examenDTO);

    /**
     * Partially updates a examen.
     *
     * @param examenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExamenDTO> partialUpdate(ExamenDTO examenDTO);

    /**
     * Get all the examen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" examen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamenDTO> findOne(Long id);

    /**
     * Delete the "id" examen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
