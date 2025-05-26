package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.NiveauAsserts.*;
import static sn.edu.ugb.curriculum.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.curriculum.IntegrationTest;
import sn.edu.ugb.curriculum.domain.Niveau;
import sn.edu.ugb.curriculum.repository.NiveauRepository;
import sn.edu.ugb.curriculum.service.dto.NiveauDTO;
import sn.edu.ugb.curriculum.service.mapper.NiveauMapper;

/**
 * Integration tests for the {@link NiveauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NiveauResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final Long DEFAULT_PROGRAMME_ID = 1L;
    private static final Long UPDATED_PROGRAMME_ID = 2L;

    private static final String ENTITY_API_URL = "/api/niveaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private NiveauMapper niveauMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNiveauMockMvc;

    private Niveau niveau;

    private Niveau insertedNiveau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createEntity() {
        return new Niveau().libelle(DEFAULT_LIBELLE).ordre(DEFAULT_ORDRE).programmeId(DEFAULT_PROGRAMME_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createUpdatedEntity() {
        return new Niveau().libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE).programmeId(UPDATED_PROGRAMME_ID);
    }

    @BeforeEach
    void initTest() {
        niveau = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNiveau != null) {
            niveauRepository.delete(insertedNiveau);
            insertedNiveau = null;
        }
    }

    @Test
    @Transactional
    void createNiveau() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);
        var returnedNiveauDTO = om.readValue(
            restNiveauMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NiveauDTO.class
        );

        // Validate the Niveau in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNiveau = niveauMapper.toEntity(returnedNiveauDTO);
        assertNiveauUpdatableFieldsEquals(returnedNiveau, getPersistedNiveau(returnedNiveau));

        insertedNiveau = returnedNiveau;
    }

    @Test
    @Transactional
    void createNiveauWithExistingId() throws Exception {
        // Create the Niveau with an existing ID
        niveau.setId(1L);
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNiveauMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        niveau.setLibelle(null);

        // Create the Niveau, which fails.
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        restNiveauMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        niveau.setOrdre(null);

        // Create the Niveau, which fails.
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        restNiveauMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProgrammeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        niveau.setProgrammeId(null);

        // Create the Niveau, which fails.
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        restNiveauMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNiveaus() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        // Get all the niveauList
        restNiveauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].programmeId").value(hasItem(DEFAULT_PROGRAMME_ID.intValue())));
    }

    @Test
    @Transactional
    void getNiveau() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        // Get the niveau
        restNiveauMockMvc
            .perform(get(ENTITY_API_URL_ID, niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(niveau.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.programmeId").value(DEFAULT_PROGRAMME_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingNiveau() throws Exception {
        // Get the niveau
        restNiveauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNiveau() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the niveau
        Niveau updatedNiveau = niveauRepository.findById(niveau.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNiveau are not directly saved in db
        em.detach(updatedNiveau);
        updatedNiveau.libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE).programmeId(UPDATED_PROGRAMME_ID);
        NiveauDTO niveauDTO = niveauMapper.toDto(updatedNiveau);

        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNiveauToMatchAllProperties(updatedNiveau);
    }

    @Test
    @Transactional
    void putNonExistingNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(niveauDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNiveauWithPatch() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the niveau using partial update
        Niveau partialUpdatedNiveau = new Niveau();
        partialUpdatedNiveau.setId(niveau.getId());

        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNiveau))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNiveauUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNiveau, niveau), getPersistedNiveau(niveau));
    }

    @Test
    @Transactional
    void fullUpdateNiveauWithPatch() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the niveau using partial update
        Niveau partialUpdatedNiveau = new Niveau();
        partialUpdatedNiveau.setId(niveau.getId());

        partialUpdatedNiveau.libelle(UPDATED_LIBELLE).ordre(UPDATED_ORDRE).programmeId(UPDATED_PROGRAMME_ID);

        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNiveau))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNiveauUpdatableFieldsEquals(partialUpdatedNiveau, getPersistedNiveau(partialUpdatedNiveau));
    }

    @Test
    @Transactional
    void patchNonExistingNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNiveau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(niveauDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Niveau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNiveau() throws Exception {
        // Initialize the database
        insertedNiveau = niveauRepository.saveAndFlush(niveau);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the niveau
        restNiveauMockMvc
            .perform(delete(ENTITY_API_URL_ID, niveau.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return niveauRepository.count();
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

    protected Niveau getPersistedNiveau(Niveau niveau) {
        return niveauRepository.findById(niveau.getId()).orElseThrow();
    }

    protected void assertPersistedNiveauToMatchAllProperties(Niveau expectedNiveau) {
        assertNiveauAllPropertiesEquals(expectedNiveau, getPersistedNiveau(expectedNiveau));
    }

    protected void assertPersistedNiveauToMatchUpdatableProperties(Niveau expectedNiveau) {
        assertNiveauAllUpdatablePropertiesEquals(expectedNiveau, getPersistedNiveau(expectedNiveau));
    }
}
