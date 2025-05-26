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
import sn.edu.ugb.student.repository.DossierEtudiantRepository;
import sn.edu.ugb.student.service.DossierEtudiantService;
import sn.edu.ugb.student.service.dto.DossierEtudiantDTO;
import sn.edu.ugb.student.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.student.domain.DossierEtudiant}.
 */
@RestController
@RequestMapping("/api/dossier-etudiants")
public class DossierEtudiantResource {

    private static final Logger LOG = LoggerFactory.getLogger(DossierEtudiantResource.class);

    private static final String ENTITY_NAME = "studentServiceDossierEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierEtudiantService dossierEtudiantService;

    private final DossierEtudiantRepository dossierEtudiantRepository;

    public DossierEtudiantResource(DossierEtudiantService dossierEtudiantService, DossierEtudiantRepository dossierEtudiantRepository) {
        this.dossierEtudiantService = dossierEtudiantService;
        this.dossierEtudiantRepository = dossierEtudiantRepository;
    }

    /**
     * {@code POST  /dossier-etudiants} : Create a new dossierEtudiant.
     *
     * @param dossierEtudiantDTO the dossierEtudiantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierEtudiantDTO, or with status {@code 400 (Bad Request)} if the dossierEtudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DossierEtudiantDTO> createDossierEtudiant(@Valid @RequestBody DossierEtudiantDTO dossierEtudiantDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DossierEtudiant : {}", dossierEtudiantDTO);
        if (dossierEtudiantDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossierEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dossierEtudiantDTO = dossierEtudiantService.save(dossierEtudiantDTO);
        return ResponseEntity.created(new URI("/api/dossier-etudiants/" + dossierEtudiantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dossierEtudiantDTO.getId().toString()))
            .body(dossierEtudiantDTO);
    }

    /**
     * {@code PUT  /dossier-etudiants/:id} : Updates an existing dossierEtudiant.
     *
     * @param id the id of the dossierEtudiantDTO to save.
     * @param dossierEtudiantDTO the dossierEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the dossierEtudiantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DossierEtudiantDTO> updateDossierEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DossierEtudiantDTO dossierEtudiantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DossierEtudiant : {}, {}", id, dossierEtudiantDTO);
        if (dossierEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dossierEtudiantDTO = dossierEtudiantService.update(dossierEtudiantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierEtudiantDTO.getId().toString()))
            .body(dossierEtudiantDTO);
    }

    /**
     * {@code PATCH  /dossier-etudiants/:id} : Partial updates given fields of an existing dossierEtudiant, field will ignore if it is null
     *
     * @param id the id of the dossierEtudiantDTO to save.
     * @param dossierEtudiantDTO the dossierEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the dossierEtudiantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dossierEtudiantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DossierEtudiantDTO> partialUpdateDossierEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DossierEtudiantDTO dossierEtudiantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DossierEtudiant partially : {}, {}", id, dossierEtudiantDTO);
        if (dossierEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DossierEtudiantDTO> result = dossierEtudiantService.partialUpdate(dossierEtudiantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierEtudiantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dossier-etudiants} : get all the dossierEtudiants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierEtudiants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DossierEtudiantDTO>> getAllDossierEtudiants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DossierEtudiants");
        Page<DossierEtudiantDTO> page = dossierEtudiantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dossier-etudiants/:id} : get the "id" dossierEtudiant.
     *
     * @param id the id of the dossierEtudiantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierEtudiantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DossierEtudiantDTO> getDossierEtudiant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DossierEtudiant : {}", id);
        Optional<DossierEtudiantDTO> dossierEtudiantDTO = dossierEtudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierEtudiantDTO);
    }

    /**
     * {@code DELETE  /dossier-etudiants/:id} : delete the "id" dossierEtudiant.
     *
     * @param id the id of the dossierEtudiantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDossierEtudiant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DossierEtudiant : {}", id);
        dossierEtudiantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
