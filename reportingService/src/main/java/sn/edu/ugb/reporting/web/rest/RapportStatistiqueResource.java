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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.edu.ugb.reporting.repository.RapportStatistiqueRepository;
import sn.edu.ugb.reporting.service.RapportStatistiqueService;
import sn.edu.ugb.reporting.service.dto.RapportStatistiqueDTO;
import sn.edu.ugb.reporting.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.reporting.domain.RapportStatistique}.
 */
@RestController
@RequestMapping("/api/rapport-statistiques")
public class RapportStatistiqueResource {

    private static final Logger LOG = LoggerFactory.getLogger(RapportStatistiqueResource.class);

    private static final String ENTITY_NAME = "reportingServiceRapportStatistique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapportStatistiqueService rapportStatistiqueService;

    private final RapportStatistiqueRepository rapportStatistiqueRepository;

    public RapportStatistiqueResource(
        RapportStatistiqueService rapportStatistiqueService,
        RapportStatistiqueRepository rapportStatistiqueRepository
    ) {
        this.rapportStatistiqueService = rapportStatistiqueService;
        this.rapportStatistiqueRepository = rapportStatistiqueRepository;
    }

    /**
     * {@code POST  /rapport-statistiques} : Create a new rapportStatistique.
     *
     * @param rapportStatistiqueDTO the rapportStatistiqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapportStatistiqueDTO, or with status {@code 400 (Bad Request)} if the rapportStatistique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RapportStatistiqueDTO> createRapportStatistique(@Valid @RequestBody RapportStatistiqueDTO rapportStatistiqueDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RapportStatistique : {}", rapportStatistiqueDTO);
        if (rapportStatistiqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new rapportStatistique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rapportStatistiqueDTO = rapportStatistiqueService.save(rapportStatistiqueDTO);
        return ResponseEntity.created(new URI("/api/rapport-statistiques/" + rapportStatistiqueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rapportStatistiqueDTO.getId().toString()))
            .body(rapportStatistiqueDTO);
    }

    /**
     * {@code PUT  /rapport-statistiques/:id} : Updates an existing rapportStatistique.
     *
     * @param id the id of the rapportStatistiqueDTO to save.
     * @param rapportStatistiqueDTO the rapportStatistiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportStatistiqueDTO,
     * or with status {@code 400 (Bad Request)} if the rapportStatistiqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapportStatistiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RapportStatistiqueDTO> updateRapportStatistique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RapportStatistiqueDTO rapportStatistiqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RapportStatistique : {}, {}", id, rapportStatistiqueDTO);
        if (rapportStatistiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportStatistiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportStatistiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rapportStatistiqueDTO = rapportStatistiqueService.update(rapportStatistiqueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportStatistiqueDTO.getId().toString()))
            .body(rapportStatistiqueDTO);
    }

    /**
     * {@code PATCH  /rapport-statistiques/:id} : Partial updates given fields of an existing rapportStatistique, field will ignore if it is null
     *
     * @param id the id of the rapportStatistiqueDTO to save.
     * @param rapportStatistiqueDTO the rapportStatistiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportStatistiqueDTO,
     * or with status {@code 400 (Bad Request)} if the rapportStatistiqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rapportStatistiqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rapportStatistiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RapportStatistiqueDTO> partialUpdateRapportStatistique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RapportStatistiqueDTO rapportStatistiqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RapportStatistique partially : {}, {}", id, rapportStatistiqueDTO);
        if (rapportStatistiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportStatistiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportStatistiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RapportStatistiqueDTO> result = rapportStatistiqueService.partialUpdate(rapportStatistiqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportStatistiqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rapport-statistiques} : get all the rapportStatistiques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapportStatistiques in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RapportStatistiqueDTO>> getAllRapportStatistiques(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RapportStatistiques");
        Page<RapportStatistiqueDTO> page = rapportStatistiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rapport-statistiques/:id} : get the "id" rapportStatistique.
     *
     * @param id the id of the rapportStatistiqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapportStatistiqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RapportStatistiqueDTO> getRapportStatistique(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RapportStatistique : {}", id);
        Optional<RapportStatistiqueDTO> rapportStatistiqueDTO = rapportStatistiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rapportStatistiqueDTO);
    }

    /**
     * {@code DELETE  /rapport-statistiques/:id} : delete the "id" rapportStatistique.
     *
     * @param id the id of the rapportStatistiqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapportStatistique(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RapportStatistique : {}", id);
        rapportStatistiqueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
