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
import sn.edu.ugb.curriculum.repository.MatiereProgrammeRepository;
import sn.edu.ugb.curriculum.service.MatiereProgrammeService;
import sn.edu.ugb.curriculum.service.dto.MatiereProgrammeDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.MatiereProgramme}.
 */
@RestController
@RequestMapping("/api/matiere-programmes")
public class MatiereProgrammeResource {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereProgrammeResource.class);

    private static final String ENTITY_NAME = "curriculumServiceMatiereProgramme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatiereProgrammeService matiereProgrammeService;

    private final MatiereProgrammeRepository matiereProgrammeRepository;

    public MatiereProgrammeResource(
        MatiereProgrammeService matiereProgrammeService,
        MatiereProgrammeRepository matiereProgrammeRepository
    ) {
        this.matiereProgrammeService = matiereProgrammeService;
        this.matiereProgrammeRepository = matiereProgrammeRepository;
    }

    /**
     * {@code POST  /matiere-programmes} : Create a new matiereProgramme.
     *
     * @param matiereProgrammeDTO the matiereProgrammeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matiereProgrammeDTO, or with status {@code 400 (Bad Request)} if the matiereProgramme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MatiereProgrammeDTO> createMatiereProgramme(@Valid @RequestBody MatiereProgrammeDTO matiereProgrammeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MatiereProgramme : {}", matiereProgrammeDTO);
        if (matiereProgrammeDTO.getId() != null) {
            throw new BadRequestAlertException("A new matiereProgramme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        matiereProgrammeDTO = matiereProgrammeService.save(matiereProgrammeDTO);
        return ResponseEntity.created(new URI("/api/matiere-programmes/" + matiereProgrammeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, matiereProgrammeDTO.getId().toString()))
            .body(matiereProgrammeDTO);
    }

    /**
     * {@code PUT  /matiere-programmes/:id} : Updates an existing matiereProgramme.
     *
     * @param id the id of the matiereProgrammeDTO to save.
     * @param matiereProgrammeDTO the matiereProgrammeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereProgrammeDTO,
     * or with status {@code 400 (Bad Request)} if the matiereProgrammeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matiereProgrammeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatiereProgrammeDTO> updateMatiereProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatiereProgrammeDTO matiereProgrammeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MatiereProgramme : {}, {}", id, matiereProgrammeDTO);
        if (matiereProgrammeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereProgrammeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereProgrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        matiereProgrammeDTO = matiereProgrammeService.update(matiereProgrammeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereProgrammeDTO.getId().toString()))
            .body(matiereProgrammeDTO);
    }

    /**
     * {@code PATCH  /matiere-programmes/:id} : Partial updates given fields of an existing matiereProgramme, field will ignore if it is null
     *
     * @param id the id of the matiereProgrammeDTO to save.
     * @param matiereProgrammeDTO the matiereProgrammeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereProgrammeDTO,
     * or with status {@code 400 (Bad Request)} if the matiereProgrammeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matiereProgrammeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matiereProgrammeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatiereProgrammeDTO> partialUpdateMatiereProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatiereProgrammeDTO matiereProgrammeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MatiereProgramme partially : {}, {}", id, matiereProgrammeDTO);
        if (matiereProgrammeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereProgrammeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereProgrammeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatiereProgrammeDTO> result = matiereProgrammeService.partialUpdate(matiereProgrammeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereProgrammeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matiere-programmes} : get all the matiereProgrammes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matiereProgrammes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MatiereProgrammeDTO>> getAllMatiereProgrammes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of MatiereProgrammes");
        Page<MatiereProgrammeDTO> page = matiereProgrammeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matiere-programmes/:id} : get the "id" matiereProgramme.
     *
     * @param id the id of the matiereProgrammeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matiereProgrammeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatiereProgrammeDTO> getMatiereProgramme(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MatiereProgramme : {}", id);
        Optional<MatiereProgrammeDTO> matiereProgrammeDTO = matiereProgrammeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matiereProgrammeDTO);
    }

    /**
     * {@code DELETE  /matiere-programmes/:id} : delete the "id" matiereProgramme.
     *
     * @param id the id of the matiereProgrammeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiereProgramme(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MatiereProgramme : {}", id);
        matiereProgrammeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
