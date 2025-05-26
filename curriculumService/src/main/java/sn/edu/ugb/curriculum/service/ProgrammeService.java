package sn.edu.ugb.curriculum.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.curriculum.service.dto.ProgrammeDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.curriculum.domain.Programme}.
 */
public interface ProgrammeService {
    /**
     * Save a programme.
     *
     * @param programmeDTO the entity to save.
     * @return the persisted entity.
     */
    ProgrammeDTO save(ProgrammeDTO programmeDTO);

    /**
     * Updates a programme.
     *
     * @param programmeDTO the entity to update.
     * @return the persisted entity.
     */
    ProgrammeDTO update(ProgrammeDTO programmeDTO);

    /**
     * Partially updates a programme.
     *
     * @param programmeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgrammeDTO> partialUpdate(ProgrammeDTO programmeDTO);

    /**
     * Get all the programmes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProgrammeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" programme.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgrammeDTO> findOne(Long id);

    /**
     * Delete the "id" programme.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
