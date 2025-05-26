package sn.edu.ugb.reporting.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.reporting.service.dto.RapportStatistiqueDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.reporting.domain.RapportStatistique}.
 */
public interface RapportStatistiqueService {
    /**
     * Save a rapportStatistique.
     *
     * @param rapportStatistiqueDTO the entity to save.
     * @return the persisted entity.
     */
    RapportStatistiqueDTO save(RapportStatistiqueDTO rapportStatistiqueDTO);

    /**
     * Updates a rapportStatistique.
     *
     * @param rapportStatistiqueDTO the entity to update.
     * @return the persisted entity.
     */
    RapportStatistiqueDTO update(RapportStatistiqueDTO rapportStatistiqueDTO);

    /**
     * Partially updates a rapportStatistique.
     *
     * @param rapportStatistiqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RapportStatistiqueDTO> partialUpdate(RapportStatistiqueDTO rapportStatistiqueDTO);

    /**
     * Get all the rapportStatistiques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RapportStatistiqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rapportStatistique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RapportStatistiqueDTO> findOne(Long id);

    /**
     * Delete the "id" rapportStatistique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
