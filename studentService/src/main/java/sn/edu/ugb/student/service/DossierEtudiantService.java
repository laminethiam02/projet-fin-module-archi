package sn.edu.ugb.student.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.student.service.dto.DossierEtudiantDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.student.domain.DossierEtudiant}.
 */
public interface DossierEtudiantService {
    /**
     * Save a dossierEtudiant.
     *
     * @param dossierEtudiantDTO the entity to save.
     * @return the persisted entity.
     */
    DossierEtudiantDTO save(DossierEtudiantDTO dossierEtudiantDTO);

    /**
     * Updates a dossierEtudiant.
     *
     * @param dossierEtudiantDTO the entity to update.
     * @return the persisted entity.
     */
    DossierEtudiantDTO update(DossierEtudiantDTO dossierEtudiantDTO);

    /**
     * Partially updates a dossierEtudiant.
     *
     * @param dossierEtudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierEtudiantDTO> partialUpdate(DossierEtudiantDTO dossierEtudiantDTO);

    /**
     * Get all the dossierEtudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DossierEtudiantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dossierEtudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierEtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" dossierEtudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
