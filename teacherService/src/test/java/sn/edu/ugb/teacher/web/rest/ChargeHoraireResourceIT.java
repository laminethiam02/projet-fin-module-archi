package sn.edu.ugb.teacher.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.teacher.domain.ChargeHoraireAsserts.*;
import static sn.edu.ugb.teacher.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.edu.ugb.teacher.IntegrationTest;
import sn.edu.ugb.teacher.domain.ChargeHoraire;
import sn.edu.ugb.teacher.repository.ChargeHoraireRepository;
import sn.edu.ugb.teacher.service.dto.ChargeHoraireDTO;
import sn.edu.ugb.teacher.service.mapper.ChargeHoraireMapper;

/**
 * Integration tests for the {@link ChargeHoraireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChargeHoraireResourceIT {

    private static final Integer DEFAULT_NOMBRE_HEURES = 1;
    private static final Integer UPDATED_NOMBRE_HEURES = 2;

    private static final Long DEFAULT_CONTRAT_ID = 1L;
    private static final Long UPDATED_CONTRAT_ID = 2L;

    private static final Long DEFAULT_CLASSE_ID = 1L;
    private static final Long UPDATED_CLASSE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/charge-horaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChargeHoraireRepository chargeHoraireRepository;

    @Autowired
    private ChargeHoraireMapper chargeHoraireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChargeHoraireMockMvc;

    private ChargeHoraire chargeHoraire;

    private ChargeHoraire insertedChargeHoraire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChargeHoraire createEntity() {
        return new ChargeHoraire().nombreHeures(DEFAULT_NOMBRE_HEURES).contratId(DEFAULT_CONTRAT_ID).classeId(DEFAULT_CLASSE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChargeHoraire createUpdatedEntity() {
        return new ChargeHoraire().nombreHeures(UPDATED_NOMBRE_HEURES).contratId(UPDATED_CONTRAT_ID).classeId(UPDATED_CLASSE_ID);
    }

    @BeforeEach
    void initTest() {
        chargeHoraire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChargeHoraire != null) {
            chargeHoraireRepository.delete(insertedChargeHoraire);
            insertedChargeHoraire = null;
        }
    }

    @Test
    @Transactional
    void createChargeHoraire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);
        var returnedChargeHoraireDTO = om.readValue(
            restChargeHoraireMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(chargeHoraireDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChargeHoraireDTO.class
        );

        // Validate the ChargeHoraire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChargeHoraire = chargeHoraireMapper.toEntity(returnedChargeHoraireDTO);
        assertChargeHoraireUpdatableFieldsEquals(returnedChargeHoraire, getPersistedChargeHoraire(returnedChargeHoraire));

        insertedChargeHoraire = returnedChargeHoraire;
    }

    @Test
    @Transactional
    void createChargeHoraireWithExistingId() throws Exception {
        // Create the ChargeHoraire with an existing ID
        chargeHoraire.setId(1L);
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChargeHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreHeuresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chargeHoraire.setNombreHeures(null);

        // Create the ChargeHoraire, which fails.
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        restChargeHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContratIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chargeHoraire.setContratId(null);

        // Create the ChargeHoraire, which fails.
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        restChargeHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClasseIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chargeHoraire.setClasseId(null);

        // Create the ChargeHoraire, which fails.
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        restChargeHoraireMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChargeHoraires() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        // Get all the chargeHoraireList
        restChargeHoraireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chargeHoraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreHeures").value(hasItem(DEFAULT_NOMBRE_HEURES)))
            .andExpect(jsonPath("$.[*].contratId").value(hasItem(DEFAULT_CONTRAT_ID.intValue())))
            .andExpect(jsonPath("$.[*].classeId").value(hasItem(DEFAULT_CLASSE_ID.intValue())));
    }

    @Test
    @Transactional
    void getChargeHoraire() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        // Get the chargeHoraire
        restChargeHoraireMockMvc
            .perform(get(ENTITY_API_URL_ID, chargeHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chargeHoraire.getId().intValue()))
            .andExpect(jsonPath("$.nombreHeures").value(DEFAULT_NOMBRE_HEURES))
            .andExpect(jsonPath("$.contratId").value(DEFAULT_CONTRAT_ID.intValue()))
            .andExpect(jsonPath("$.classeId").value(DEFAULT_CLASSE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingChargeHoraire() throws Exception {
        // Get the chargeHoraire
        restChargeHoraireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChargeHoraire() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chargeHoraire
        ChargeHoraire updatedChargeHoraire = chargeHoraireRepository.findById(chargeHoraire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChargeHoraire are not directly saved in db
        em.detach(updatedChargeHoraire);
        updatedChargeHoraire.nombreHeures(UPDATED_NOMBRE_HEURES).contratId(UPDATED_CONTRAT_ID).classeId(UPDATED_CLASSE_ID);
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(updatedChargeHoraire);

        restChargeHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chargeHoraireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChargeHoraireToMatchAllProperties(updatedChargeHoraire);
    }

    @Test
    @Transactional
    void putNonExistingChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chargeHoraireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChargeHoraireWithPatch() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chargeHoraire using partial update
        ChargeHoraire partialUpdatedChargeHoraire = new ChargeHoraire();
        partialUpdatedChargeHoraire.setId(chargeHoraire.getId());

        partialUpdatedChargeHoraire.nombreHeures(UPDATED_NOMBRE_HEURES).contratId(UPDATED_CONTRAT_ID).classeId(UPDATED_CLASSE_ID);

        restChargeHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChargeHoraire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChargeHoraire))
            )
            .andExpect(status().isOk());

        // Validate the ChargeHoraire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChargeHoraireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChargeHoraire, chargeHoraire),
            getPersistedChargeHoraire(chargeHoraire)
        );
    }

    @Test
    @Transactional
    void fullUpdateChargeHoraireWithPatch() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chargeHoraire using partial update
        ChargeHoraire partialUpdatedChargeHoraire = new ChargeHoraire();
        partialUpdatedChargeHoraire.setId(chargeHoraire.getId());

        partialUpdatedChargeHoraire.nombreHeures(UPDATED_NOMBRE_HEURES).contratId(UPDATED_CONTRAT_ID).classeId(UPDATED_CLASSE_ID);

        restChargeHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChargeHoraire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChargeHoraire))
            )
            .andExpect(status().isOk());

        // Validate the ChargeHoraire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChargeHoraireUpdatableFieldsEquals(partialUpdatedChargeHoraire, getPersistedChargeHoraire(partialUpdatedChargeHoraire));
    }

    @Test
    @Transactional
    void patchNonExistingChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chargeHoraireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChargeHoraire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chargeHoraire.setId(longCount.incrementAndGet());

        // Create the ChargeHoraire
        ChargeHoraireDTO chargeHoraireDTO = chargeHoraireMapper.toDto(chargeHoraire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChargeHoraireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chargeHoraireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChargeHoraire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChargeHoraire() throws Exception {
        // Initialize the database
        insertedChargeHoraire = chargeHoraireRepository.saveAndFlush(chargeHoraire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chargeHoraire
        restChargeHoraireMockMvc
            .perform(delete(ENTITY_API_URL_ID, chargeHoraire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chargeHoraireRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ChargeHoraire getPersistedChargeHoraire(ChargeHoraire chargeHoraire) {
        return chargeHoraireRepository.findById(chargeHoraire.getId()).orElseThrow();
    }

    protected void assertPersistedChargeHoraireToMatchAllProperties(ChargeHoraire expectedChargeHoraire) {
        assertChargeHoraireAllPropertiesEquals(expectedChargeHoraire, getPersistedChargeHoraire(expectedChargeHoraire));
    }

    protected void assertPersistedChargeHoraireToMatchUpdatableProperties(ChargeHoraire expectedChargeHoraire) {
        assertChargeHoraireAllUpdatablePropertiesEquals(expectedChargeHoraire, getPersistedChargeHoraire(expectedChargeHoraire));
    }
}
