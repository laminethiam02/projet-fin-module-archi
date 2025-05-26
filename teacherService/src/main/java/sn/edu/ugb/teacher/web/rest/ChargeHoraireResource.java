package sn.edu.ugb.teacher.web.rest;

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
import sn.edu.ugb.teacher.repository.ChargeHoraireRepository;
import sn.edu.ugb.teacher.service.ChargeHoraireService;
import sn.edu.ugb.teacher.service.dto.ChargeHoraireDTO;
import sn.edu.ugb.teacher.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.edu.ugb.teacher.domain.ChargeHoraire}.
 */
@RestController
@RequestMapping("/api/charge-horaires")
public class ChargeHoraireResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChargeHoraireResource.class);

    private static final String ENTITY_NAME = "teacherServiceChargeHoraire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChargeHoraireService chargeHoraireService;

    private final ChargeHoraireRepository chargeHoraireRepository;

    public ChargeHoraireResource(ChargeHoraireService chargeHoraireService, ChargeHoraireRepository chargeHoraireRepository) {
        this.chargeHoraireService = chargeHoraireService;
        this.chargeHoraireRepository = chargeHoraireRepository;
    }

    /**
     * {@code POST  /charge-horaires} : Create a new chargeHoraire.
     *
     * @param chargeHoraireDTO the chargeHoraireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chargeHoraireDTO, or with status {@code 400 (Bad Request)} if the chargeHoraire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChargeHoraireDTO> createChargeHoraire(@Valid @RequestBody ChargeHoraireDTO chargeHoraireDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ChargeHoraire : {}", chargeHoraireDTO);
        if (chargeHoraireDTO.getId() != null) {
            throw new BadRequestAlertException("A new chargeHoraire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chargeHoraireDTO = chargeHoraireService.save(chargeHoraireDTO);
        return ResponseEntity.created(new URI("/api/charge-horaires/" + chargeHoraireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chargeHoraireDTO.getId().toString()))
            .body(chargeHoraireDTO);
    }

    /**
     * {@code PUT  /charge-horaires/:id} : Updates an existing chargeHoraire.
     *
     * @param id the id of the chargeHoraireDTO to save.
     * @param chargeHoraireDTO the chargeHoraireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chargeHoraireDTO,
     * or with status {@code 400 (Bad Request)} if the chargeHoraireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chargeHoraireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChargeHoraireDTO> updateChargeHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChargeHoraireDTO chargeHoraireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChargeHoraire : {}, {}", id, chargeHoraireDTO);
        if (chargeHoraireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chargeHoraireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chargeHoraireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chargeHoraireDTO = chargeHoraireService.update(chargeHoraireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chargeHoraireDTO.getId().toString()))
            .body(chargeHoraireDTO);
    }

    /**
     * {@code PATCH  /charge-horaires/:id} : Partial updates given fields of an existing chargeHoraire, field will ignore if it is null
     *
     * @param id the id of the chargeHoraireDTO to save.
     * @param chargeHoraireDTO the chargeHoraireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chargeHoraireDTO,
     * or with status {@code 400 (Bad Request)} if the chargeHoraireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chargeHoraireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chargeHoraireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChargeHoraireDTO> partialUpdateChargeHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChargeHoraireDTO chargeHoraireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChargeHoraire partially : {}, {}", id, chargeHoraireDTO);
        if (chargeHoraireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chargeHoraireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chargeHoraireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChargeHoraireDTO> result = chargeHoraireService.partialUpdate(chargeHoraireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chargeHoraireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /charge-horaires} : get all the chargeHoraires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chargeHoraires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChargeHoraireDTO>> getAllChargeHoraires(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ChargeHoraires");
        Page<ChargeHoraireDTO> page = chargeHoraireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /charge-horaires/:id} : get the "id" chargeHoraire.
     *
     * @param id the id of the chargeHoraireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chargeHoraireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChargeHoraireDTO> getChargeHoraire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChargeHoraire : {}", id);
        Optional<ChargeHoraireDTO> chargeHoraireDTO = chargeHoraireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chargeHoraireDTO);
    }

    /**
     * {@code DELETE  /charge-horaires/:id} : delete the "id" chargeHoraire.
     *
     * @param id the id of the chargeHoraireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargeHoraire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChargeHoraire : {}", id);
        chargeHoraireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
