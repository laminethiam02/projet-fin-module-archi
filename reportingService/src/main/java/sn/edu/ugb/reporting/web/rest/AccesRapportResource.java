package sn.edu.ugb.reporting.web.rest;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.ugb.reporting.repository.AccesRapportRepository;
import sn.edu.ugb.reporting.service.AccesRapportService;
import sn.edu.ugb.reporting.service.dto.AccesRapportDTO;
import sn.edu.ugb.reporting.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.reporting.domain.AccesRapport}.
 */
@RestController
@RequestMapping("/api/acces-rapports")
public class AccesRapportResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccesRapportResource.class);

    private static final String ENTITY_NAME = "reportingServiceAccesRapport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccesRapportService accesRapportService;

    private final AccesRapportRepository accesRapportRepository;

    public AccesRapportResource(AccesRapportService accesRapportService, AccesRapportRepository accesRapportRepository) {
        this.accesRapportService = accesRapportService;
        this.accesRapportRepository = accesRapportRepository;
    }

    /**
     * {@code POST  /acces-rapports} : Create a new accesRapport.
     *
     * @param accesRapportDTO the accesRapportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accesRapportDTO, or with status {@code 400 (Bad Request)} if the accesRapport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccesRapportDTO> createAccesRapport(@Valid @RequestBody AccesRapportDTO accesRapportDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AccesRapport : {}", accesRapportDTO);
        if (accesRapportDTO.getId() != null) {
            throw new BadRequestAlertException("A new accesRapport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accesRapportDTO = accesRapportService.save(accesRapportDTO);
        return ResponseEntity.created(new URI("/api/acces-rapports/" + accesRapportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accesRapportDTO.getId().toString()))
            .body(accesRapportDTO);
    }

    /**
     * {@code PUT  /acces-rapports/:id} : Updates an existing accesRapport.
     *
     * @param id the id of the accesRapportDTO to save.
     * @param accesRapportDTO the accesRapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accesRapportDTO,
     * or with status {@code 400 (Bad Request)} if the accesRapportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accesRapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccesRapportDTO> updateAccesRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccesRapportDTO accesRapportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccesRapport : {}, {}", id, accesRapportDTO);
        if (accesRapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accesRapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accesRapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accesRapportDTO = accesRapportService.update(accesRapportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accesRapportDTO.getId().toString()))
            .body(accesRapportDTO);
    }

    /**
     * {@code PATCH  /acces-rapports/:id} : Partial updates given fields of an existing accesRapport, field will ignore if it is null
     *
     * @param id the id of the accesRapportDTO to save.
     * @param accesRapportDTO the accesRapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accesRapportDTO,
     * or with status {@code 400 (Bad Request)} if the accesRapportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accesRapportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accesRapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccesRapportDTO> partialUpdateAccesRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccesRapportDTO accesRapportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AccesRapport partially : {}, {}", id, accesRapportDTO);
        if (accesRapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accesRapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accesRapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccesRapportDTO> result = accesRapportService.partialUpdate(accesRapportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accesRapportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acces-rapports} : get all the accesRapports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accesRapports in body.
     */
    @GetMapping("")
    public List<AccesRapportDTO> getAllAccesRapports() {
        LOG.debug("REST request to get all AccesRapports");
        return accesRapportService.findAll();
    }

    /**
     * {@code GET  /acces-rapports/:id} : get the "id" accesRapport.
     *
     * @param id the id of the accesRapportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accesRapportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccesRapportDTO> getAccesRapport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccesRapport : {}", id);
        Optional<AccesRapportDTO> accesRapportDTO = accesRapportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accesRapportDTO);
    }

    /**
     * {@code DELETE  /acces-rapports/:id} : delete the "id" accesRapport.
     *
     * @param id the id of the accesRapportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccesRapport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccesRapport : {}", id);
        accesRapportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
