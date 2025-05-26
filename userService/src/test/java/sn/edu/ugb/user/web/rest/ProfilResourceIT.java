package sn.edu.ugb.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.user.domain.ProfilAsserts.*;
import static sn.edu.ugb.user.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.user.IntegrationTest;
import sn.edu.ugb.user.domain.Profil;
import sn.edu.ugb.user.repository.ProfilRepository;
import sn.edu.ugb.user.service.dto.ProfilDTO;
import sn.edu.ugb.user.service.mapper.ProfilMapper;

/**
 * Integration tests for the {@link ProfilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfilResourceIT {

    private static final String DEFAULT_NOM_COMPLET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPTE_ID = 1L;
    private static final Long UPDATED_COMPTE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilMockMvc;

    private Profil profil;

    private Profil insertedProfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createEntity() {
        return new Profil()
            .nomComplet(DEFAULT_NOM_COMPLET)
            .adresse(DEFAULT_ADRESSE)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .compteId(DEFAULT_COMPTE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createUpdatedEntity() {
        return new Profil()
            .nomComplet(UPDATED_NOM_COMPLET)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .compteId(UPDATED_COMPTE_ID);
    }

    @BeforeEach
    void initTest() {
        profil = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfil != null) {
            profilRepository.delete(insertedProfil);
            insertedProfil = null;
        }
    }

    @Test
    @Transactional
    void createProfil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);
        var returnedProfilDTO = om.readValue(
            restProfilMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfilDTO.class
        );

        // Validate the Profil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfil = profilMapper.toEntity(returnedProfilDTO);
        assertProfilUpdatableFieldsEquals(returnedProfil, getPersistedProfil(returnedProfil));

        insertedProfil = returnedProfil;
    }

    @Test
    @Transactional
    void createProfilWithExistingId() throws Exception {
        // Create the Profil with an existing ID
        profil.setId(1L);
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCompletIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profil.setNomComplet(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        restProfilMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profil.setEmail(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        restProfilMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompteIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profil.setCompteId(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        restProfilMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfils() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        // Get all the profilList
        restProfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].compteId").value(hasItem(DEFAULT_COMPTE_ID.intValue())));
    }

    @Test
    @Transactional
    void getProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        // Get the profil
        restProfilMockMvc
            .perform(get(ENTITY_API_URL_ID, profil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profil.getId().intValue()))
            .andExpect(jsonPath("$.nomComplet").value(DEFAULT_NOM_COMPLET))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.compteId").value(DEFAULT_COMPTE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProfil() throws Exception {
        // Get the profil
        restProfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil
        Profil updatedProfil = profilRepository.findById(profil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfil are not directly saved in db
        em.detach(updatedProfil);
        updatedProfil
            .nomComplet(UPDATED_NOM_COMPLET)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .compteId(UPDATED_COMPTE_ID);
        ProfilDTO profilDTO = profilMapper.toDto(updatedProfil);

        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfilToMatchAllProperties(updatedProfil);
    }

    @Test
    @Transactional
    void putNonExistingProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profilDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.nomComplet(UPDATED_NOM_COMPLET).adresse(UPDATED_ADRESSE).telephone(UPDATED_TELEPHONE);

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProfil, profil), getPersistedProfil(profil));
    }

    @Test
    @Transactional
    void fullUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil
            .nomComplet(UPDATED_NOM_COMPLET)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .compteId(UPDATED_COMPTE_ID);

        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfil))
            )
            .andExpect(status().isOk());

        // Validate the Profil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfilUpdatableFieldsEquals(partialUpdatedProfil, getPersistedProfil(partialUpdatedProfil));
    }

    @Test
    @Transactional
    void patchNonExistingProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profilDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfilMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profilDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfil() throws Exception {
        // Initialize the database
        insertedProfil = profilRepository.saveAndFlush(profil);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profil
        restProfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, profil.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profilRepository.count();
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

    protected Profil getPersistedProfil(Profil profil) {
        return profilRepository.findById(profil.getId()).orElseThrow();
    }

    protected void assertPersistedProfilToMatchAllProperties(Profil expectedProfil) {
        assertProfilAllPropertiesEquals(expectedProfil, getPersistedProfil(expectedProfil));
    }

    protected void assertPersistedProfilToMatchUpdatableProperties(Profil expectedProfil) {
        assertProfilAllUpdatablePropertiesEquals(expectedProfil, getPersistedProfil(expectedProfil));
    }
}
