package sn.edu.ugb.reporting.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.reporting.service.dto.IndicateurDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.reporting.domain.Indicateur}.
 */
public interface IndicateurService {
    /**
     * Save a indicateur.
     *
     * @param indicateurDTO the entity to save.
     * @return the persisted entity.
     */
    IndicateurDTO save(IndicateurDTO indicateurDTO);

    /**
     * Updates a indicateur.
     *
     * @param indicateurDTO the entity to update.
     * @return the persisted entity.
     */
    IndicateurDTO update(IndicateurDTO indicateurDTO);

    /**
     * Partially updates a indicateur.
     *
     * @param indicateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IndicateurDTO> partialUpdate(IndicateurDTO indicateurDTO);

    /**
     * Get all the indicateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IndicateurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" indicateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IndicateurDTO> findOne(Long id);

    /**
     * Delete the "id" indicateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
