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
import sn.edu.ugb.reporting.repository.IndicateurRepository;
import sn.edu.ugb.reporting.service.IndicateurService;
import sn.edu.ugb.reporting.service.dto.IndicateurDTO;
import sn.edu.ugb.reporting.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.reporting.domain.Indicateur}.
 */
@RestController
@RequestMapping("/api/indicateurs")
public class IndicateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndicateurResource.class);

    private static final String ENTITY_NAME = "reportingServiceIndicateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndicateurService indicateurService;

    private final IndicateurRepository indicateurRepository;

    public IndicateurResource(IndicateurService indicateurService, IndicateurRepository indicateurRepository) {
        this.indicateurService = indicateurService;
        this.indicateurRepository = indicateurRepository;
    }

    /**
     * {@code POST  /indicateurs} : Create a new indicateur.
     *
     * @param indicateurDTO the indicateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indicateurDTO, or with status {@code 400 (Bad Request)} if the indicateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndicateurDTO> createIndicateur(@Valid @RequestBody IndicateurDTO indicateurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Indicateur : {}", indicateurDTO);
        if (indicateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new indicateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indicateurDTO = indicateurService.save(indicateurDTO);
        return ResponseEntity.created(new URI("/api/indicateurs/" + indicateurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, indicateurDTO.getId().toString()))
            .body(indicateurDTO);
    }

    /**
     * {@code PUT  /indicateurs/:id} : Updates an existing indicateur.
     *
     * @param id the id of the indicateurDTO to save.
     * @param indicateurDTO the indicateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indicateurDTO,
     * or with status {@code 400 (Bad Request)} if the indicateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indicateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndicateurDTO> updateIndicateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IndicateurDTO indicateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Indicateur : {}, {}", id, indicateurDTO);
        if (indicateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indicateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indicateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indicateurDTO = indicateurService.update(indicateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indicateurDTO.getId().toString()))
            .body(indicateurDTO);
    }

    /**
     * {@code PATCH  /indicateurs/:id} : Partial updates given fields of an existing indicateur, field will ignore if it is null
     *
     * @param id the id of the indicateurDTO to save.
     * @param indicateurDTO the indicateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indicateurDTO,
     * or with status {@code 400 (Bad Request)} if the indicateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the indicateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the indicateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndicateurDTO> partialUpdateIndicateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IndicateurDTO indicateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Indicateur partially : {}, {}", id, indicateurDTO);
        if (indicateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indicateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indicateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndicateurDTO> result = indicateurService.partialUpdate(indicateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indicateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /indicateurs} : get all the indicateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indicateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndicateurDTO>> getAllIndicateurs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Indicateurs");
        Page<IndicateurDTO> page = indicateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indicateurs/:id} : get the "id" indicateur.
     *
     * @param id the id of the indicateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indicateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndicateurDTO> getIndicateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Indicateur : {}", id);
        Optional<IndicateurDTO> indicateurDTO = indicateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indicateurDTO);
    }

    /**
     * {@code DELETE  /indicateurs/:id} : delete the "id" indicateur.
     *
     * @param id the id of the indicateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndicateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Indicateur : {}", id);
        indicateurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
