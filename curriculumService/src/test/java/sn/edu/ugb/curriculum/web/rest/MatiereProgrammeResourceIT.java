package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.MatiereProgrammeAsserts.*;
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
import sn.edu.ugb.curriculum.domain.MatiereProgramme;
import sn.edu.ugb.curriculum.repository.MatiereProgrammeRepository;
import sn.edu.ugb.curriculum.service.dto.MatiereProgrammeDTO;
import sn.edu.ugb.curriculum.service.mapper.MatiereProgrammeMapper;

/**
 * Integration tests for the {@link MatiereProgrammeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatiereProgrammeResourceIT {

    private static final String DEFAULT_NOM_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MATIERE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEURES_COURS = 1;
    private static final Integer UPDATED_HEURES_COURS = 2;

    private static final Long DEFAULT_UE_ID = 1L;
    private static final Long UPDATED_UE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/matiere-programmes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MatiereProgrammeRepository matiereProgrammeRepository;

    @Autowired
    private MatiereProgrammeMapper matiereProgrammeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatiereProgrammeMockMvc;

    private MatiereProgramme matiereProgramme;

    private MatiereProgramme insertedMatiereProgramme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatiereProgramme createEntity() {
        return new MatiereProgramme().nomMatiere(DEFAULT_NOM_MATIERE).heuresCours(DEFAULT_HEURES_COURS).ueId(DEFAULT_UE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatiereProgramme createUpdatedEntity() {
        return new MatiereProgramme().nomMatiere(UPDATED_NOM_MATIERE).heuresCours(UPDATED_HEURES_COURS).ueId(UPDATED_UE_ID);
    }

    @BeforeEach
    void initTest() {
        matiereProgramme = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMatiereProgramme != null) {
            matiereProgrammeRepository.delete(insertedMatiereProgramme);
            insertedMatiereProgramme = null;
        }
    }

    @Test
    @Transactional
    void createMatiereProgramme() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);
        var returnedMatiereProgrammeDTO = om.readValue(
            restMatiereProgrammeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(matiereProgrammeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MatiereProgrammeDTO.class
        );

        // Validate the MatiereProgramme in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMatiereProgramme = matiereProgrammeMapper.toEntity(returnedMatiereProgrammeDTO);
        assertMatiereProgrammeUpdatableFieldsEquals(returnedMatiereProgramme, getPersistedMatiereProgramme(returnedMatiereProgramme));

        insertedMatiereProgramme = returnedMatiereProgramme;
    }

    @Test
    @Transactional
    void createMatiereProgrammeWithExistingId() throws Exception {
        // Create the MatiereProgramme with an existing ID
        matiereProgramme.setId(1L);
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatiereProgrammeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomMatiereIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiereProgramme.setNomMatiere(null);

        // Create the MatiereProgramme, which fails.
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        restMatiereProgrammeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matiereProgramme.setUeId(null);

        // Create the MatiereProgramme, which fails.
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        restMatiereProgrammeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatiereProgrammes() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        // Get all the matiereProgrammeList
        restMatiereProgrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiereProgramme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomMatiere").value(hasItem(DEFAULT_NOM_MATIERE)))
            .andExpect(jsonPath("$.[*].heuresCours").value(hasItem(DEFAULT_HEURES_COURS)))
            .andExpect(jsonPath("$.[*].ueId").value(hasItem(DEFAULT_UE_ID.intValue())));
    }

    @Test
    @Transactional
    void getMatiereProgramme() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        // Get the matiereProgramme
        restMatiereProgrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, matiereProgramme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matiereProgramme.getId().intValue()))
            .andExpect(jsonPath("$.nomMatiere").value(DEFAULT_NOM_MATIERE))
            .andExpect(jsonPath("$.heuresCours").value(DEFAULT_HEURES_COURS))
            .andExpect(jsonPath("$.ueId").value(DEFAULT_UE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMatiereProgramme() throws Exception {
        // Get the matiereProgramme
        restMatiereProgrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatiereProgramme() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiereProgramme
        MatiereProgramme updatedMatiereProgramme = matiereProgrammeRepository.findById(matiereProgramme.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMatiereProgramme are not directly saved in db
        em.detach(updatedMatiereProgramme);
        updatedMatiereProgramme.nomMatiere(UPDATED_NOM_MATIERE).heuresCours(UPDATED_HEURES_COURS).ueId(UPDATED_UE_ID);
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(updatedMatiereProgramme);

        restMatiereProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereProgrammeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMatiereProgrammeToMatchAllProperties(updatedMatiereProgramme);
    }

    @Test
    @Transactional
    void putNonExistingMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiereProgrammeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatiereProgrammeWithPatch() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiereProgramme using partial update
        MatiereProgramme partialUpdatedMatiereProgramme = new MatiereProgramme();
        partialUpdatedMatiereProgramme.setId(matiereProgramme.getId());

        partialUpdatedMatiereProgramme.ueId(UPDATED_UE_ID);

        restMatiereProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiereProgramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatiereProgramme))
            )
            .andExpect(status().isOk());

        // Validate the MatiereProgramme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatiereProgrammeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMatiereProgramme, matiereProgramme),
            getPersistedMatiereProgramme(matiereProgramme)
        );
    }

    @Test
    @Transactional
    void fullUpdateMatiereProgrammeWithPatch() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matiereProgramme using partial update
        MatiereProgramme partialUpdatedMatiereProgramme = new MatiereProgramme();
        partialUpdatedMatiereProgramme.setId(matiereProgramme.getId());

        partialUpdatedMatiereProgramme.nomMatiere(UPDATED_NOM_MATIERE).heuresCours(UPDATED_HEURES_COURS).ueId(UPDATED_UE_ID);

        restMatiereProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiereProgramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatiereProgramme))
            )
            .andExpect(status().isOk());

        // Validate the MatiereProgramme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatiereProgrammeUpdatableFieldsEquals(
            partialUpdatedMatiereProgramme,
            getPersistedMatiereProgramme(partialUpdatedMatiereProgramme)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matiereProgrammeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatiereProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matiereProgramme.setId(longCount.incrementAndGet());

        // Create the MatiereProgramme
        MatiereProgrammeDTO matiereProgrammeDTO = matiereProgrammeMapper.toDto(matiereProgramme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matiereProgrammeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatiereProgramme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatiereProgramme() throws Exception {
        // Initialize the database
        insertedMatiereProgramme = matiereProgrammeRepository.saveAndFlush(matiereProgramme);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the matiereProgramme
        restMatiereProgrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, matiereProgramme.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return matiereProgrammeRepository.count();
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

    protected MatiereProgramme getPersistedMatiereProgramme(MatiereProgramme matiereProgramme) {
        return matiereProgrammeRepository.findById(matiereProgramme.getId()).orElseThrow();
    }

    protected void assertPersistedMatiereProgrammeToMatchAllProperties(MatiereProgramme expectedMatiereProgramme) {
        assertMatiereProgrammeAllPropertiesEquals(expectedMatiereProgramme, getPersistedMatiereProgramme(expectedMatiereProgramme));
    }

    protected void assertPersistedMatiereProgrammeToMatchUpdatableProperties(MatiereProgramme expectedMatiereProgramme) {
        assertMatiereProgrammeAllUpdatablePropertiesEquals(
            expectedMatiereProgramme,
            getPersistedMatiereProgramme(expectedMatiereProgramme)
        );
    }
}
