package sn.edu.ugb.teacher.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.edu.ugb.teacher.service.dto.ClasseDTO;

/**
 * Service Interface for managing {@link sn.edu.ugb.teacher.domain.Classe}.
 */
public interface ClasseService {
    /**
     * Save a classe.
     *
     * @param classeDTO the entity to save.
     * @return the persisted entity.
     */
    ClasseDTO save(ClasseDTO classeDTO);

    /**
     * Updates a classe.
     *
     * @param classeDTO the entity to update.
     * @return the persisted entity.
     */
    ClasseDTO update(ClasseDTO classeDTO);

    /**
     * Partially updates a classe.
     *
     * @param classeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClasseDTO> partialUpdate(ClasseDTO classeDTO);

    /**
     * Get all the classes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClasseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClasseDTO> findOne(Long id);

    /**
     * Delete the "id" classe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
