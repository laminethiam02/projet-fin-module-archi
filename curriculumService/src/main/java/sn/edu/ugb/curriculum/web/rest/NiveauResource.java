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
import sn.edu.ugb.curriculum.repository.NiveauRepository;
import sn.edu.ugb.curriculum.service.NiveauService;
import sn.edu.ugb.curriculum.service.dto.NiveauDTO;
import sn.edu.ugb.curriculum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.curriculum.domain.Niveau}.
 */
@RestController
@RequestMapping("/api/niveaus")
public class NiveauResource {

    private static final Logger LOG = LoggerFactory.getLogger(NiveauResource.class);

    private static final String ENTITY_NAME = "curriculumServiceNiveau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NiveauService niveauService;

    private final NiveauRepository niveauRepository;

    public NiveauResource(NiveauService niveauService, NiveauRepository niveauRepository) {
        this.niveauService = niveauService;
        this.niveauRepository = niveauRepository;
    }

    /**
     * {@code POST  /niveaus} : Create a new niveau.
     *
     * @param niveauDTO the niveauDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new niveauDTO, or with status {@code 400 (Bad Request)} if the niveau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NiveauDTO> createNiveau(@Valid @RequestBody NiveauDTO niveauDTO) throws URISyntaxException {
        LOG.debug("REST request to save Niveau : {}", niveauDTO);
        if (niveauDTO.getId() != null) {
            throw new BadRequestAlertException("A new niveau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        niveauDTO = niveauService.save(niveauDTO);
        return ResponseEntity.created(new URI("/api/niveaus/" + niveauDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, niveauDTO.getId().toString()))
            .body(niveauDTO);
    }

    /**
     * {@code PUT  /niveaus/:id} : Updates an existing niveau.
     *
     * @param id the id of the niveauDTO to save.
     * @param niveauDTO the niveauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated niveauDTO,
     * or with status {@code 400 (Bad Request)} if the niveauDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the niveauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NiveauDTO> updateNiveau(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NiveauDTO niveauDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Niveau : {}, {}", id, niveauDTO);
        if (niveauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, niveauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!niveauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        niveauDTO = niveauService.update(niveauDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, niveauDTO.getId().toString()))
            .body(niveauDTO);
    }

    /**
     * {@code PATCH  /niveaus/:id} : Partial updates given fields of an existing niveau, field will ignore if it is null
     *
     * @param id the id of the niveauDTO to save.
     * @param niveauDTO the niveauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated niveauDTO,
     * or with status {@code 400 (Bad Request)} if the niveauDTO is not valid,
     * or with status {@code 404 (Not Found)} if the niveauDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the niveauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NiveauDTO> partialUpdateNiveau(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NiveauDTO niveauDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Niveau partially : {}, {}", id, niveauDTO);
        if (niveauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, niveauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!niveauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NiveauDTO> result = niveauService.partialUpdate(niveauDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, niveauDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /niveaus} : get all the niveaus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of niveaus in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NiveauDTO>> getAllNiveaus(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Niveaus");
        Page<NiveauDTO> page = niveauService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /niveaus/:id} : get the "id" niveau.
     *
     * @param id the id of the niveauDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the niveauDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NiveauDTO> getNiveau(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Niveau : {}", id);
        Optional<NiveauDTO> niveauDTO = niveauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(niveauDTO);
    }

    /**
     * {@code DELETE  /niveaus/:id} : delete the "id" niveau.
     *
     * @param id the id of the niveauDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Niveau : {}", id);
        niveauService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
