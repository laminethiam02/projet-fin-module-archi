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
import sn.edu.ugb.grade.repository.BaremeRepository;
import sn.edu.ugb.grade.service.BaremeService;
import sn.edu.ugb.grade.service.dto.BaremeDTO;
import sn.edu.ugb.grade.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.grade.domain.Bareme}.
 */
@RestController
@RequestMapping("/api/baremes")
public class BaremeResource {

    private static final Logger LOG = LoggerFactory.getLogger(BaremeResource.class);

    private static final String ENTITY_NAME = "noteServiceBareme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaremeService baremeService;

    private final BaremeRepository baremeRepository;

    public BaremeResource(BaremeService baremeService, BaremeRepository baremeRepository) {
        this.baremeService = baremeService;
        this.baremeRepository = baremeRepository;
    }

    /**
     * {@code POST  /baremes} : Create a new bareme.
     *
     * @param baremeDTO the baremeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baremeDTO, or with status {@code 400 (Bad Request)} if the bareme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BaremeDTO> createBareme(@Valid @RequestBody BaremeDTO baremeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bareme : {}", baremeDTO);
        if (baremeDTO.getId() != null) {
            throw new BadRequestAlertException("A new bareme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        baremeDTO = baremeService.save(baremeDTO);
        return ResponseEntity.created(new URI("/api/baremes/" + baremeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, baremeDTO.getId().toString()))
            .body(baremeDTO);
    }

    /**
     * {@code PUT  /baremes/:id} : Updates an existing bareme.
     *
     * @param id the id of the baremeDTO to save.
     * @param baremeDTO the baremeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baremeDTO,
     * or with status {@code 400 (Bad Request)} if the baremeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baremeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaremeDTO> updateBareme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BaremeDTO baremeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bareme : {}, {}", id, baremeDTO);
        if (baremeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baremeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baremeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        baremeDTO = baremeService.update(baremeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baremeDTO.getId().toString()))
            .body(baremeDTO);
    }

    /**
     * {@code PATCH  /baremes/:id} : Partial updates given fields of an existing bareme, field will ignore if it is null
     *
     * @param id the id of the baremeDTO to save.
     * @param baremeDTO the baremeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baremeDTO,
     * or with status {@code 400 (Bad Request)} if the baremeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the baremeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the baremeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaremeDTO> partialUpdateBareme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BaremeDTO baremeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bareme partially : {}, {}", id, baremeDTO);
        if (baremeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baremeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baremeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaremeDTO> result = baremeService.partialUpdate(baremeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baremeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /baremes} : get all the baremes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baremes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BaremeDTO>> getAllBaremes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Baremes");
        Page<BaremeDTO> page = baremeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /baremes/:id} : get the "id" bareme.
     *
     * @param id the id of the baremeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baremeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaremeDTO> getBareme(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bareme : {}", id);
        Optional<BaremeDTO> baremeDTO = baremeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baremeDTO);
    }

    /**
     * {@code DELETE  /baremes/:id} : delete the "id" bareme.
     *
     * @param id the id of the baremeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBareme(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bareme : {}", id);
        baremeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
