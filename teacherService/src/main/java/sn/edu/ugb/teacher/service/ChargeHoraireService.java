package sn.edu.ugb.teacher.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.teacher.service.dto.ChargeHoraireDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.teacher.domain.ChargeHoraire}.
 */
public interface ChargeHoraireService {
    /**
     * Save a chargeHoraire.
     *
     * @param chargeHoraireDTO the entity to save.
     * @return the persisted entity.
     */
    ChargeHoraireDTO save(ChargeHoraireDTO chargeHoraireDTO);

    /**
     * Updates a chargeHoraire.
     *
     * @param chargeHoraireDTO the entity to update.
     * @return the persisted entity.
     */
    ChargeHoraireDTO update(ChargeHoraireDTO chargeHoraireDTO);

    /**
     * Partially updates a chargeHoraire.
     *
     * @param chargeHoraireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChargeHoraireDTO> partialUpdate(ChargeHoraireDTO chargeHoraireDTO);

    /**
     * Get all the chargeHoraires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChargeHoraireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" chargeHoraire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChargeHoraireDTO> findOne(Long id);

    /**
     * Delete the "id" chargeHoraire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
