package sn.edu.ugb.reporting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.reporting.domain.AccesRapportAsserts.*;
import static sn.edu.ugb.reporting.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.edu.ugb.reporting.IntegrationTest;
import sn.edu.ugb.reporting.domain.AccesRapport;
import sn.edu.ugb.reporting.repository.AccesRapportRepository;
import sn.edu.ugb.reporting.service.dto.AccesRapportDTO;
import sn.edu.ugb.reporting.service.mapper.AccesRapportMapper;

/**
 * Integration tests for the {@link AccesRapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccesRapportResourceIT {

    private static final Long DEFAULT_UTILISATEUR_ID = 1L;
    private static final Long UPDATED_UTILISATEUR_ID = 2L;

    private static final Long DEFAULT_RAPPORT_ID = 1L;
    private static final Long UPDATED_RAPPORT_ID = 2L;

    private static final Instant DEFAULT_DATE_ACCES = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACCES = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/acces-rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccesRapportRepository accesRapportRepository;

    @Autowired
    private AccesRapportMapper accesRapportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccesRapportMockMvc;

    private AccesRapport accesRapport;

    private AccesRapport insertedAccesRapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccesRapport createEntity() {
        return new AccesRapport().utilisateurId(DEFAULT_UTILISATEUR_ID).rapportId(DEFAULT_RAPPORT_ID).dateAcces(DEFAULT_DATE_ACCES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccesRapport createUpdatedEntity() {
        return new AccesRapport().utilisateurId(UPDATED_UTILISATEUR_ID).rapportId(UPDATED_RAPPORT_ID).dateAcces(UPDATED_DATE_ACCES);
    }

    @BeforeEach
    void initTest() {
        accesRapport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAccesRapport != null) {
            accesRapportRepository.delete(insertedAccesRapport);
            insertedAccesRapport = null;
        }
    }

    @Test
    @Transactional
    void createAccesRapport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);
        var returnedAccesRapportDTO = om.readValue(
            restAccesRapportMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccesRapportDTO.class
        );

        // Validate the AccesRapport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccesRapport = accesRapportMapper.toEntity(returnedAccesRapportDTO);
        assertAccesRapportUpdatableFieldsEquals(returnedAccesRapport, getPersistedAccesRapport(returnedAccesRapport));

        insertedAccesRapport = returnedAccesRapport;
    }

    @Test
    @Transactional
    void createAccesRapportWithExistingId() throws Exception {
        // Create the AccesRapport with an existing ID
        accesRapport.setId(1L);
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccesRapportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUtilisateurIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accesRapport.setUtilisateurId(null);

        // Create the AccesRapport, which fails.
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        restAccesRapportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRapportIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accesRapport.setRapportId(null);

        // Create the AccesRapport, which fails.
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        restAccesRapportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAccesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accesRapport.setDateAcces(null);

        // Create the AccesRapport, which fails.
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        restAccesRapportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccesRapports() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        // Get all the accesRapportList
        restAccesRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accesRapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].utilisateurId").value(hasItem(DEFAULT_UTILISATEUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].rapportId").value(hasItem(DEFAULT_RAPPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateAcces").value(hasItem(DEFAULT_DATE_ACCES.toString())));
    }

    @Test
    @Transactional
    void getAccesRapport() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        // Get the accesRapport
        restAccesRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, accesRapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accesRapport.getId().intValue()))
            .andExpect(jsonPath("$.utilisateurId").value(DEFAULT_UTILISATEUR_ID.intValue()))
            .andExpect(jsonPath("$.rapportId").value(DEFAULT_RAPPORT_ID.intValue()))
            .andExpect(jsonPath("$.dateAcces").value(DEFAULT_DATE_ACCES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccesRapport() throws Exception {
        // Get the accesRapport
        restAccesRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccesRapport() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accesRapport
        AccesRapport updatedAccesRapport = accesRapportRepository.findById(accesRapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccesRapport are not directly saved in db
        em.detach(updatedAccesRapport);
        updatedAccesRapport.utilisateurId(UPDATED_UTILISATEUR_ID).rapportId(UPDATED_RAPPORT_ID).dateAcces(UPDATED_DATE_ACCES);
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(updatedAccesRapport);

        restAccesRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accesRapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccesRapportToMatchAllProperties(updatedAccesRapport);
    }

    @Test
    @Transactional
    void putNonExistingAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accesRapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccesRapportWithPatch() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accesRapport using partial update
        AccesRapport partialUpdatedAccesRapport = new AccesRapport();
        partialUpdatedAccesRapport.setId(accesRapport.getId());

        partialUpdatedAccesRapport.utilisateurId(UPDATED_UTILISATEUR_ID).dateAcces(UPDATED_DATE_ACCES);

        restAccesRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccesRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccesRapport))
            )
            .andExpect(status().isOk());

        // Validate the AccesRapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccesRapportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccesRapport, accesRapport),
            getPersistedAccesRapport(accesRapport)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccesRapportWithPatch() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accesRapport using partial update
        AccesRapport partialUpdatedAccesRapport = new AccesRapport();
        partialUpdatedAccesRapport.setId(accesRapport.getId());

        partialUpdatedAccesRapport.utilisateurId(UPDATED_UTILISATEUR_ID).rapportId(UPDATED_RAPPORT_ID).dateAcces(UPDATED_DATE_ACCES);

        restAccesRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccesRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccesRapport))
            )
            .andExpect(status().isOk());

        // Validate the AccesRapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccesRapportUpdatableFieldsEquals(partialUpdatedAccesRapport, getPersistedAccesRapport(partialUpdatedAccesRapport));
    }

    @Test
    @Transactional
    void patchNonExistingAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accesRapportDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccesRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accesRapport.setId(longCount.incrementAndGet());

        // Create the AccesRapport
        AccesRapportDTO accesRapportDTO = accesRapportMapper.toDto(accesRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccesRapportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accesRapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccesRapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccesRapport() throws Exception {
        // Initialize the database
        insertedAccesRapport = accesRapportRepository.saveAndFlush(accesRapport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accesRapport
        restAccesRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, accesRapport.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accesRapportRepository.count();
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

    protected AccesRapport getPersistedAccesRapport(AccesRapport accesRapport) {
        return accesRapportRepository.findById(accesRapport.getId()).orElseThrow();
    }

    protected void assertPersistedAccesRapportToMatchAllProperties(AccesRapport expectedAccesRapport) {
        assertAccesRapportAllPropertiesEquals(expectedAccesRapport, getPersistedAccesRapport(expectedAccesRapport));
    }

    protected void assertPersistedAccesRapportToMatchUpdatableProperties(AccesRapport expectedAccesRapport) {
        assertAccesRapportAllUpdatablePropertiesEquals(expectedAccesRapport, getPersistedAccesRapport(expectedAccesRapport));
    }
}
