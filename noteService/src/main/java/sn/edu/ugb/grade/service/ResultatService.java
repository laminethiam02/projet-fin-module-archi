package sn.edu.ugb.grade.service;

import java.util.List;
import java.util.Optional;
import sn.edu.ugb.grade.service.dto.ResultatDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.grade.domain.Resultat}.
 */
public interface ResultatService {
    /**
     * Save a resultat.
     *
     * @param resultatDTO the entity to save.
     * @return the persisted entity.
     */
    ResultatDTO save(ResultatDTO resultatDTO);

    /**
     * Updates a resultat.
     *
     * @param resultatDTO the entity to update.
     * @return the persisted entity.
     */
    ResultatDTO update(ResultatDTO resultatDTO);

    /**
     * Partially updates a resultat.
     *
     * @param resultatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResultatDTO> partialUpdate(ResultatDTO resultatDTO);

    /**
     * Get all the resultats.
     *
     * @return the list of entities.
     */
    List<ResultatDTO> findAll();

    /**
     * Get the "id" resultat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResultatDTO> findOne(Long id);

    /**
     * Delete the "id" resultat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
