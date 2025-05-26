package sn.edu.ugb.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.user.domain.CompteAsserts.*;
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
import sn.edu.ugb.user.domain.Compte;
import sn.edu.ugb.user.repository.CompteRepository;
import sn.edu.ugb.user.service.dto.CompteDTO;
import sn.edu.ugb.user.service.mapper.CompteMapper;

/**
 * Integration tests for the {@link CompteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/comptes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteMapper compteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteMockMvc;

    private Compte compte;

    private Compte insertedCompte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createEntity() {
        return new Compte().login(DEFAULT_LOGIN).motDePasse(DEFAULT_MOT_DE_PASSE).actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createUpdatedEntity() {
        return new Compte().login(UPDATED_LOGIN).motDePasse(UPDATED_MOT_DE_PASSE).actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        compte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompte != null) {
            compteRepository.delete(insertedCompte);
            insertedCompte = null;
        }
    }

    @Test
    @Transactional
    void createCompte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);
        var returnedCompteDTO = om.readValue(
            restCompteMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompteDTO.class
        );

        // Validate the Compte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompte = compteMapper.toEntity(returnedCompteDTO);
        assertCompteUpdatableFieldsEquals(returnedCompte, getPersistedCompte(returnedCompte));

        insertedCompte = returnedCompte;
    }

    @Test
    @Transactional
    void createCompteWithExistingId() throws Exception {
        // Create the Compte with an existing ID
        compte.setId(1L);
        CompteDTO compteDTO = compteMapper.toDto(compte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compte.setLogin(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotDePasseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compte.setMotDePasse(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComptes() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get all the compteList
        restCompteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc
            .perform(get(ENTITY_API_URL_ID, compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompte are not directly saved in db
        em.detach(updatedCompte);
        updatedCompte.login(UPDATED_LOGIN).motDePasse(UPDATED_MOT_DE_PASSE).actif(UPDATED_ACTIF);
        CompteDTO compteDTO = compteMapper.toDto(updatedCompte);

        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompteToMatchAllProperties(updatedCompte);
    }

    @Test
    @Transactional
    void putNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte.motDePasse(UPDATED_MOT_DE_PASSE).actif(UPDATED_ACTIF);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompte, compte), getPersistedCompte(compte));
    }

    @Test
    @Transactional
    void fullUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte.login(UPDATED_LOGIN).motDePasse(UPDATED_MOT_DE_PASSE).actif(UPDATED_ACTIF);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(partialUpdatedCompte, getPersistedCompte(partialUpdatedCompte));
    }

    @Test
    @Transactional
    void patchNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compte
        restCompteMockMvc
            .perform(delete(ENTITY_API_URL_ID, compte.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compteRepository.count();
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

    protected Compte getPersistedCompte(Compte compte) {
        return compteRepository.findById(compte.getId()).orElseThrow();
    }

    protected void assertPersistedCompteToMatchAllProperties(Compte expectedCompte) {
        assertCompteAllPropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }

    protected void assertPersistedCompteToMatchUpdatableProperties(Compte expectedCompte) {
        assertCompteAllUpdatablePropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }
}
