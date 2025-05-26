package sn.edu.ugb.grade.web.rest;

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
import sn.edu.ugb.grade.repository.ExamenRepository;
import sn.edu.ugb.grade.service.ExamenService;
import sn.edu.ugb.grade.service.dto.ExamenDTO;
import sn.edu.ugb.grade.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.grade.domain.Examen}.
 */
@RestController
@RequestMapping("/api/examen")
public class ExamenResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExamenResource.class);

    private static final String ENTITY_NAME = "noteServiceExamen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamenService examenService;

    private final ExamenRepository examenRepository;

    public ExamenResource(ExamenService examenService, ExamenRepository examenRepository) {
        this.examenService = examenService;
        this.examenRepository = examenRepository;
    }

    /**
     * {@code POST  /examen} : Create a new examen.
     *
     * @param examenDTO the examenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examenDTO, or with status {@code 400 (Bad Request)} if the examen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExamenDTO> createExamen(@Valid @RequestBody ExamenDTO examenDTO) throws URISyntaxException {
        LOG.debug("REST request to save Examen : {}", examenDTO);
        if (examenDTO.getId() != null) {
            throw new BadRequestAlertException("A new examen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examenDTO = examenService.save(examenDTO);
        return ResponseEntity.created(new URI("/api/examen/" + examenDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examenDTO.getId().toString()))
            .body(examenDTO);
    }

    /**
     * {@code PUT  /examen/:id} : Updates an existing examen.
     *
     * @param id the id of the examenDTO to save.
     * @param examenDTO the examenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examenDTO,
     * or with status {@code 400 (Bad Request)} if the examenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamenDTO> updateExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamenDTO examenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Examen : {}, {}", id, examenDTO);
        if (examenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examenDTO = examenService.update(examenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examenDTO.getId().toString()))
            .body(examenDTO);
    }

    /**
     * {@code PATCH  /examen/:id} : Partial updates given fields of an existing examen, field will ignore if it is null
     *
     * @param id the id of the examenDTO to save.
     * @param examenDTO the examenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examenDTO,
     * or with status {@code 400 (Bad Request)} if the examenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamenDTO> partialUpdateExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamenDTO examenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Examen partially : {}, {}", id, examenDTO);
        if (examenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamenDTO> result = examenService.partialUpdate(examenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /examen} : get all the examen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examen in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExamenDTO>> getAllExamen(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Examen");
        Page<ExamenDTO> page = examenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examen/:id} : get the "id" examen.
     *
     * @param id the id of the examenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExamenDTO> getExamen(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Examen : {}", id);
        Optional<ExamenDTO> examenDTO = examenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examenDTO);
    }

    /**
     * {@code DELETE  /examen/:id} : delete the "id" examen.
     *
     * @param id the id of the examenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamen(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Examen : {}", id);
        examenService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
