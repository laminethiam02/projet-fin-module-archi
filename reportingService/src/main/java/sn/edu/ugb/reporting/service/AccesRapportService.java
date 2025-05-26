package sn.edu.ugb.reporting.service;

import java.util.List;
import java.util.Optional;
import sn.edu.ugb.reporting.service.dto.AccesRapportDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.reporting.domain.AccesRapport}.
 */
public interface AccesRapportService {
    /**
     * Save a accesRapport.
     *
     * @param accesRapportDTO the entity to save.
     * @return the persisted entity.
     */
    AccesRapportDTO save(AccesRapportDTO accesRapportDTO);

    /**
     * Updates a accesRapport.
     *
     * @param accesRapportDTO the entity to update.
     * @return the persisted entity.
     */
    AccesRapportDTO update(AccesRapportDTO accesRapportDTO);

    /**
     * Partially updates a accesRapport.
     *
     * @param accesRapportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccesRapportDTO> partialUpdate(AccesRapportDTO accesRapportDTO);

    /**
     * Get all the accesRapports.
     *
     * @return the list of entities.
     */
    List<AccesRapportDTO> findAll();

    /**
     * Get the "id" accesRapport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccesRapportDTO> findOne(Long id);

    /**
     * Delete the "id" accesRapport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
