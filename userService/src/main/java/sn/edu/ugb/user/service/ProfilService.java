package sn.edu.ugb.user.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.user.service.dto.ProfilDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.user.domain.Profil}.
 */
public interface ProfilService {
    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    ProfilDTO save(ProfilDTO profilDTO);

    /**
     * Updates a profil.
     *
     * @param profilDTO the entity to update.
     * @return the persisted entity.
     */
    ProfilDTO update(ProfilDTO profilDTO);

    /**
     * Partially updates a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProfilDTO> partialUpdate(ProfilDTO profilDTO);

    /**
     * Get all the profils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfilDTO> findAll(Pageable pageable);

    /**
     * Get the "id" profil.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfilDTO> findOne(Long id);

    /**
     * Delete the "id" profil.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
