package sn.edu.ugb.curriculum.web.rest;

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
import sn.edu.ugb.curriculum.repository.ProgrammeRepository;
import sn.edu.ugb.curriculum.service.ProgrammeService;
import sn.edu.ugb.curriculum.service.dto.ProgrammeDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Programme}.
 */
@RestController
@RequestMapping("/api/programmes")
public class ProgrammeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProgrammeResource.class);

    private static final String ENTITY_NAME = "curriculumServiceProgramme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgrammeService programmeService;

    private final ProgrammeRepository programmeRepository;

    public ProgrammeResource(ProgrammeService programmeService, ProgrammeRepository programmeRepository) {
        this.programmeService = programmeService;
        this.programmeRepository = programmeRepository;
    }

    /**
     * {@code POST  /programmes} : Create a new programme.
     *
     * @param programmeDTO the programmeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programmeDTO, or with status {@code 400 (Bad Request)} if the programme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProgrammeDTO> createProgramme(@Valid @RequestBody ProgrammeDTO programmeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Programme : {}", programmeDTO);
        if (programmeDTO.getId() != null) {
            throw new BadRequestAlertException("A new programme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        programmeDTO = programmeService.save(programmeDTO);
        return ResponseEntity.created(new URI("/api/programmes/" + programmeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, programmeDTO.getId().toString()))
            .body(programmeDTO);
    }

    /**
     * {@code PUT  /programmes/:id} : Updates an existing programme.
     *
     * @param id the id of the programmeDTO to save.
     * @param programmeDTO the programmeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programmeDTO,
     * or with status {@code 400 (Bad Request)} if the programmeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programmeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProgrammeDTO> updateProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProgrammeDTO programmeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Programme : {}, {}", id, programmeDTO);
        if (programmeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programmeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        programmeDTO = programmeService.update(programmeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programmeDTO.getId().toString()))
            .body(programmeDTO);
    }

    /**
     * {@code PATCH  /programmes/:id} : Partial updates given fields of an existing programme, field will ignore if it is null
     *
     * @param id the id of the programmeDTO to save.
     * @param programmeDTO the programmeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programmeDTO,
     * or with status {@code 400 (Bad Request)} if the programmeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programmeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programmeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgrammeDTO> partialUpdateProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProgrammeDTO programmeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Programme partially : {}, {}", id, programmeDTO);
        if (programmeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programmeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgrammeDTO> result = programmeService.partialUpdate(programmeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programmeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /programmes} : get all the programmes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programmes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProgrammeDTO>> getAllProgrammes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Programmes");
        Page<ProgrammeDTO> page = programmeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programmes/:id} : get the "id" programme.
     *
     * @param id the id of the programmeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programmeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProgrammeDTO> getProgramme(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Programme : {}", id);
        Optional<ProgrammeDTO> programmeDTO = programmeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programmeDTO);
    }

    /**
     * {@code DELETE  /programmes/:id} : delete the "id" programme.
     *
     * @param id the id of the programmeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramme(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Programme : {}", id);
        programmeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
