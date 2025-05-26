package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.MatiereProgrammeDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.MatiereProgramme}.
 */
public interface MatiereProgrammeService {
    /**
     * Save a matiereProgramme.
     *
     * @param matiereProgrammeDTO the entity to save.
     * @return the persisted entity.
     */
    MatiereProgrammeDTO save(MatiereProgrammeDTO matiereProgrammeDTO);

    /**
     * Updates a matiereProgramme.
     *
     * @param matiereProgrammeDTO the entity to update.
     * @return the persisted entity.
     */
    MatiereProgrammeDTO update(MatiereProgrammeDTO matiereProgrammeDTO);

    /**
     * Partially updates a matiereProgramme.
     *
     * @param matiereProgrammeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MatiereProgrammeDTO> partialUpdate(MatiereProgrammeDTO matiereProgrammeDTO);

    /**
     * Get all the matiereProgrammes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MatiereProgrammeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" matiereProgramme.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MatiereProgrammeDTO> findOne(Long id);

    /**
     * Delete the "id" matiereProgramme.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
