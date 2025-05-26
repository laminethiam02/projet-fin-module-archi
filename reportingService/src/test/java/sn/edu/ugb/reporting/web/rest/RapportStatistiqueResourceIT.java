package sn.edu.ugb.reporting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.reporting.domain.RapportStatistiqueAsserts.*;
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
import sn.edu.ugb.reporting.domain.RapportStatistique;
import sn.edu.ugb.reporting.repository.RapportStatistiqueRepository;
import sn.edu.ugb.reporting.service.dto.RapportStatistiqueDTO;
import sn.edu.ugb.reporting.service.mapper.RapportStatistiqueMapper;

/**
 * Integration tests for the {@link RapportStatistiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportStatistiqueResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_GENERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GENERATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_INDICATEUR_ID = 1L;
    private static final Long UPDATED_INDICATEUR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/rapport-statistiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RapportStatistiqueRepository rapportStatistiqueRepository;

    @Autowired
    private RapportStatistiqueMapper rapportStatistiqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportStatistiqueMockMvc;

    private RapportStatistique rapportStatistique;

    private RapportStatistique insertedRapportStatistique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportStatistique createEntity() {
        return new RapportStatistique()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .dateGeneration(DEFAULT_DATE_GENERATION)
            .indicateurId(DEFAULT_INDICATEUR_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportStatistique createUpdatedEntity() {
        return new RapportStatistique()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .indicateurId(UPDATED_INDICATEUR_ID);
    }

    @BeforeEach
    void initTest() {
        rapportStatistique = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRapportStatistique != null) {
            rapportStatistiqueRepository.delete(insertedRapportStatistique);
            insertedRapportStatistique = null;
        }
    }

    @Test
    @Transactional
    void createRapportStatistique() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);
        var returnedRapportStatistiqueDTO = om.readValue(
            restRapportStatistiqueMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(rapportStatistiqueDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RapportStatistiqueDTO.class
        );

        // Validate the RapportStatistique in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRapportStatistique = rapportStatistiqueMapper.toEntity(returnedRapportStatistiqueDTO);
        assertRapportStatistiqueUpdatableFieldsEquals(
            returnedRapportStatistique,
            getPersistedRapportStatistique(returnedRapportStatistique)
        );

        insertedRapportStatistique = returnedRapportStatistique;
    }

    @Test
    @Transactional
    void createRapportStatistiqueWithExistingId() throws Exception {
        // Create the RapportStatistique with an existing ID
        rapportStatistique.setId(1L);
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportStatistiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapportStatistique.setTitre(null);

        // Create the RapportStatistique, which fails.
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        restRapportStatistiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateGenerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapportStatistique.setDateGeneration(null);

        // Create the RapportStatistique, which fails.
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        restRapportStatistiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIndicateurIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapportStatistique.setIndicateurId(null);

        // Create the RapportStatistique, which fails.
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        restRapportStatistiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRapportStatistiques() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        // Get all the rapportStatistiqueList
        restRapportStatistiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapportStatistique.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].indicateurId").value(hasItem(DEFAULT_INDICATEUR_ID.intValue())));
    }

    @Test
    @Transactional
    void getRapportStatistique() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        // Get the rapportStatistique
        restRapportStatistiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, rapportStatistique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapportStatistique.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateGeneration").value(DEFAULT_DATE_GENERATION.toString()))
            .andExpect(jsonPath("$.indicateurId").value(DEFAULT_INDICATEUR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRapportStatistique() throws Exception {
        // Get the rapportStatistique
        restRapportStatistiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRapportStatistique() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapportStatistique
        RapportStatistique updatedRapportStatistique = rapportStatistiqueRepository.findById(rapportStatistique.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRapportStatistique are not directly saved in db
        em.detach(updatedRapportStatistique);
        updatedRapportStatistique
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .indicateurId(UPDATED_INDICATEUR_ID);
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(updatedRapportStatistique);

        restRapportStatistiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportStatistiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRapportStatistiqueToMatchAllProperties(updatedRapportStatistique);
    }

    @Test
    @Transactional
    void putNonExistingRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportStatistiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportStatistiqueWithPatch() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapportStatistique using partial update
        RapportStatistique partialUpdatedRapportStatistique = new RapportStatistique();
        partialUpdatedRapportStatistique.setId(rapportStatistique.getId());

        partialUpdatedRapportStatistique.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).indicateurId(UPDATED_INDICATEUR_ID);

        restRapportStatistiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapportStatistique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapportStatistique))
            )
            .andExpect(status().isOk());

        // Validate the RapportStatistique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportStatistiqueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRapportStatistique, rapportStatistique),
            getPersistedRapportStatistique(rapportStatistique)
        );
    }

    @Test
    @Transactional
    void fullUpdateRapportStatistiqueWithPatch() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapportStatistique using partial update
        RapportStatistique partialUpdatedRapportStatistique = new RapportStatistique();
        partialUpdatedRapportStatistique.setId(rapportStatistique.getId());

        partialUpdatedRapportStatistique
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .indicateurId(UPDATED_INDICATEUR_ID);

        restRapportStatistiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapportStatistique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapportStatistique))
            )
            .andExpect(status().isOk());

        // Validate the RapportStatistique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportStatistiqueUpdatableFieldsEquals(
            partialUpdatedRapportStatistique,
            getPersistedRapportStatistique(partialUpdatedRapportStatistique)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapportStatistiqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapportStatistique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapportStatistique.setId(longCount.incrementAndGet());

        // Create the RapportStatistique
        RapportStatistiqueDTO rapportStatistiqueDTO = rapportStatistiqueMapper.toDto(rapportStatistique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportStatistiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportStatistiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RapportStatistique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapportStatistique() throws Exception {
        // Initialize the database
        insertedRapportStatistique = rapportStatistiqueRepository.saveAndFlush(rapportStatistique);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rapportStatistique
        restRapportStatistiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapportStatistique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rapportStatistiqueRepository.count();
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

    protected RapportStatistique getPersistedRapportStatistique(RapportStatistique rapportStatistique) {
        return rapportStatistiqueRepository.findById(rapportStatistique.getId()).orElseThrow();
    }

    protected void assertPersistedRapportStatistiqueToMatchAllProperties(RapportStatistique expectedRapportStatistique) {
        assertRapportStatistiqueAllPropertiesEquals(expectedRapportStatistique, getPersistedRapportStatistique(expectedRapportStatistique));
    }

    protected void assertPersistedRapportStatistiqueToMatchUpdatableProperties(RapportStatistique expectedRapportStatistique) {
        assertRapportStatistiqueAllUpdatablePropertiesEquals(
            expectedRapportStatistique,
            getPersistedRapportStatistique(expectedRapportStatistique)
        );
    }
}
