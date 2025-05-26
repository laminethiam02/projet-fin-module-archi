package sn.edu.ugb.student.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.edu.ugb.student.repository.ReleveNoteRepository;
import sn.edu.ugb.student.service.ReleveNoteService;
import sn.edu.ugb.student.service.dto.ReleveNoteDTO;
import sn.edu.ugb.student.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.student.domain.ReleveNote}.
 */
@RestController
@RequestMapping("/api/releve-notes")
public class ReleveNoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReleveNoteResource.class);

    private static final String ENTITY_NAME = "studentServiceReleveNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleveNoteService releveNoteService;

    private final ReleveNoteRepository releveNoteRepository;

    public ReleveNoteResource(ReleveNoteService releveNoteService, ReleveNoteRepository releveNoteRepository) {
        this.releveNoteService = releveNoteService;
        this.releveNoteRepository = releveNoteRepository;
    }

    /**
     * {@code POST  /releve-notes} : Create a new releveNote.
     *
     * @param releveNoteDTO the releveNoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releveNoteDTO, or with status {@code 400 (Bad Request)} if the releveNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReleveNoteDTO> createReleveNote(@Valid @RequestBody ReleveNoteDTO releveNoteDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReleveNote : {}", releveNoteDTO);
        if (releveNoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new releveNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        releveNoteDTO = releveNoteService.save(releveNoteDTO);
        return ResponseEntity.created(new URI("/api/releve-notes/" + releveNoteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, releveNoteDTO.getId().toString()))
            .body(releveNoteDTO);
    }

    /**
     * {@code PUT  /releve-notes/:id} : Updates an existing releveNote.
     *
     * @param id the id of the releveNoteDTO to save.
     * @param releveNoteDTO the releveNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveNoteDTO,
     * or with status {@code 400 (Bad Request)} if the releveNoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releveNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReleveNoteDTO> updateReleveNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReleveNoteDTO releveNoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReleveNote : {}, {}", id, releveNoteDTO);
        if (releveNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        releveNoteDTO = releveNoteService.update(releveNoteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveNoteDTO.getId().toString()))
            .body(releveNoteDTO);
    }

    /**
     * {@code PATCH  /releve-notes/:id} : Partial updates given fields of an existing releveNote, field will ignore if it is null
     *
     * @param id the id of the releveNoteDTO to save.
     * @param releveNoteDTO the releveNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releveNoteDTO,
     * or with status {@code 400 (Bad Request)} if the releveNoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the releveNoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the releveNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleveNoteDTO> partialUpdateReleveNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReleveNoteDTO releveNoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReleveNote partially : {}, {}", id, releveNoteDTO);
        if (releveNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releveNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releveNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleveNoteDTO> result = releveNoteService.partialUpdate(releveNoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, releveNoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /releve-notes} : get all the releveNotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releveNotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReleveNoteDTO>> getAllReleveNotes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ReleveNotes");
        Page<ReleveNoteDTO> page = releveNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /releve-notes/:id} : get the "id" releveNote.
     *
     * @param id the id of the releveNoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releveNoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReleveNoteDTO> getReleveNote(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReleveNote : {}", id);
        Optional<ReleveNoteDTO> releveNoteDTO = releveNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(releveNoteDTO);
    }

    /**
     * {@code DELETE  /releve-notes/:id} : delete the "id" releveNote.
     *
     * @param id the id of the releveNoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReleveNote(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReleveNote : {}", id);
        releveNoteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
