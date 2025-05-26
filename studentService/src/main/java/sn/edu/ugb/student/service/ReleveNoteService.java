package sn.edu.ugb.student.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.student.service.dto.ReleveNoteDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.student.domain.ReleveNote}.
 */
public interface ReleveNoteService {
    /**
     * Save a releveNote.
     *
     * @param releveNoteDTO the entity to save.
     * @return the persisted entity.
     */
    ReleveNoteDTO save(ReleveNoteDTO releveNoteDTO);

    /**
     * Updates a releveNote.
     *
     * @param releveNoteDTO the entity to update.
     * @return the persisted entity.
     */
    ReleveNoteDTO update(ReleveNoteDTO releveNoteDTO);

    /**
     * Partially updates a releveNote.
     *
     * @param releveNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReleveNoteDTO> partialUpdate(ReleveNoteDTO releveNoteDTO);

    /**
     * Get all the releveNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReleveNoteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" releveNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReleveNoteDTO> findOne(Long id);

    /**
     * Delete the "id" releveNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
