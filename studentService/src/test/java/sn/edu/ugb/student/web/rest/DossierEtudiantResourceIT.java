package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.DossierEtudiantAsserts.*;
import static sn.edu.ugb.student.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.edu.ugb.student.IntegrationTest;
import sn.edu.ugb.student.domain.DossierEtudiant;
import sn.edu.ugb.student.repository.DossierEtudiantRepository;
import sn.edu.ugb.student.service.dto.DossierEtudiantDTO;
import sn.edu.ugb.student.service.mapper.DossierEtudiantMapper;

/**
 * Integration tests for the {@link DossierEtudiantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierEtudiantResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PROFIL_ID = 1L;
    private static final Long UPDATED_PROFIL_ID = 2L;

    private static final String ENTITY_API_URL = "/api/dossier-etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DossierEtudiantRepository dossierEtudiantRepository;

    @Autowired
    private DossierEtudiantMapper dossierEtudiantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDossierEtudiantMockMvc;

    private DossierEtudiant dossierEtudiant;

    private DossierEtudiant insertedDossierEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierEtudiant createEntity() {
        return new DossierEtudiant()
            .matricule(DEFAULT_MATRICULE)
            .nationalite(DEFAULT_NATIONALITE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .profilId(DEFAULT_PROFIL_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierEtudiant createUpdatedEntity() {
        return new DossierEtudiant()
            .matricule(UPDATED_MATRICULE)
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .profilId(UPDATED_PROFIL_ID);
    }

    @BeforeEach
    void initTest() {
        dossierEtudiant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDossierEtudiant != null) {
            dossierEtudiantRepository.delete(insertedDossierEtudiant);
            insertedDossierEtudiant = null;
        }
    }

    @Test
    @Transactional
    void createDossierEtudiant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);
        var returnedDossierEtudiantDTO = om.readValue(
            restDossierEtudiantMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(dossierEtudiantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DossierEtudiantDTO.class
        );

        // Validate the DossierEtudiant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDossierEtudiant = dossierEtudiantMapper.toEntity(returnedDossierEtudiantDTO);
        assertDossierEtudiantUpdatableFieldsEquals(returnedDossierEtudiant, getPersistedDossierEtudiant(returnedDossierEtudiant));

        insertedDossierEtudiant = returnedDossierEtudiant;
    }

    @Test
    @Transactional
    void createDossierEtudiantWithExistingId() throws Exception {
        // Create the DossierEtudiant with an existing ID
        dossierEtudiant.setId(1L);
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dossierEtudiant.setMatricule(null);

        // Create the DossierEtudiant, which fails.
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        restDossierEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfilIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dossierEtudiant.setProfilId(null);

        // Create the DossierEtudiant, which fails.
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        restDossierEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDossierEtudiants() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        // Get all the dossierEtudiantList
        restDossierEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierEtudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].profilId").value(hasItem(DEFAULT_PROFIL_ID.intValue())));
    }

    @Test
    @Transactional
    void getDossierEtudiant() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        // Get the dossierEtudiant
        restDossierEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, dossierEtudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossierEtudiant.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.profilId").value(DEFAULT_PROFIL_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDossierEtudiant() throws Exception {
        // Get the dossierEtudiant
        restDossierEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDossierEtudiant() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dossierEtudiant
        DossierEtudiant updatedDossierEtudiant = dossierEtudiantRepository.findById(dossierEtudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDossierEtudiant are not directly saved in db
        em.detach(updatedDossierEtudiant);
        updatedDossierEtudiant
            .matricule(UPDATED_MATRICULE)
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .profilId(UPDATED_PROFIL_ID);
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(updatedDossierEtudiant);

        restDossierEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierEtudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDossierEtudiantToMatchAllProperties(updatedDossierEtudiant);
    }

    @Test
    @Transactional
    void putNonExistingDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierEtudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDossierEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dossierEtudiant using partial update
        DossierEtudiant partialUpdatedDossierEtudiant = new DossierEtudiant();
        partialUpdatedDossierEtudiant.setId(dossierEtudiant.getId());

        partialUpdatedDossierEtudiant.dateNaissance(UPDATED_DATE_NAISSANCE);

        restDossierEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDossierEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DossierEtudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDossierEtudiantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDossierEtudiant, dossierEtudiant),
            getPersistedDossierEtudiant(dossierEtudiant)
        );
    }

    @Test
    @Transactional
    void fullUpdateDossierEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dossierEtudiant using partial update
        DossierEtudiant partialUpdatedDossierEtudiant = new DossierEtudiant();
        partialUpdatedDossierEtudiant.setId(dossierEtudiant.getId());

        partialUpdatedDossierEtudiant
            .matricule(UPDATED_MATRICULE)
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .profilId(UPDATED_PROFIL_ID);

        restDossierEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDossierEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DossierEtudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDossierEtudiantUpdatableFieldsEquals(
            partialUpdatedDossierEtudiant,
            getPersistedDossierEtudiant(partialUpdatedDossierEtudiant)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossierEtudiantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDossierEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dossierEtudiant.setId(longCount.incrementAndGet());

        // Create the DossierEtudiant
        DossierEtudiantDTO dossierEtudiantDTO = dossierEtudiantMapper.toDto(dossierEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dossierEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDossierEtudiant() throws Exception {
        // Initialize the database
        insertedDossierEtudiant = dossierEtudiantRepository.saveAndFlush(dossierEtudiant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dossierEtudiant
        restDossierEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossierEtudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dossierEtudiantRepository.count();
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

    protected DossierEtudiant getPersistedDossierEtudiant(DossierEtudiant dossierEtudiant) {
        return dossierEtudiantRepository.findById(dossierEtudiant.getId()).orElseThrow();
    }

    protected void assertPersistedDossierEtudiantToMatchAllProperties(DossierEtudiant expectedDossierEtudiant) {
        assertDossierEtudiantAllPropertiesEquals(expectedDossierEtudiant, getPersistedDossierEtudiant(expectedDossierEtudiant));
    }

    protected void assertPersistedDossierEtudiantToMatchUpdatableProperties(DossierEtudiant expectedDossierEtudiant) {
        assertDossierEtudiantAllUpdatablePropertiesEquals(expectedDossierEtudiant, getPersistedDossierEtudiant(expectedDossierEtudiant));
    }
}
