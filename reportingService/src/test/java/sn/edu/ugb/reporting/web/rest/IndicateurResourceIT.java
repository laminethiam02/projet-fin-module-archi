package sn.edu.ugb.reporting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.reporting.domain.IndicateurAsserts.*;
import static sn.edu.ugb.reporting.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.reporting.IntegrationTest;
import sn.edu.ugb.reporting.domain.Indicateur;
import sn.edu.ugb.reporting.repository.IndicateurRepository;
import sn.edu.ugb.reporting.service.dto.IndicateurDTO;
import sn.edu.ugb.reporting.service.mapper.IndicateurMapper;

/**
 * Integration tests for the {@link IndicateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndicateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Float DEFAULT_VALEUR = 1F;
    private static final Float UPDATED_VALEUR = 2F;

    private static final String ENTITY_API_URL = "/api/indicateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndicateurRepository indicateurRepository;

    @Autowired
    private IndicateurMapper indicateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndicateurMockMvc;

    private Indicateur indicateur;

    private Indicateur insertedIndicateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indicateur createEntity() {
        return new Indicateur().nom(DEFAULT_NOM).valeur(DEFAULT_VALEUR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indicateur createUpdatedEntity() {
        return new Indicateur().nom(UPDATED_NOM).valeur(UPDATED_VALEUR);
    }

    @BeforeEach
    void initTest() {
        indicateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndicateur != null) {
            indicateurRepository.delete(insertedIndicateur);
            insertedIndicateur = null;
        }
    }

    @Test
    @Transactional
    void createIndicateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);
        var returnedIndicateurDTO = om.readValue(
            restIndicateurMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indicateurDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndicateurDTO.class
        );

        // Validate the Indicateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIndicateur = indicateurMapper.toEntity(returnedIndicateurDTO);
        assertIndicateurUpdatableFieldsEquals(returnedIndicateur, getPersistedIndicateur(returnedIndicateur));

        insertedIndicateur = returnedIndicateur;
    }

    @Test
    @Transactional
    void createIndicateurWithExistingId() throws Exception {
        // Create the Indicateur with an existing ID
        indicateur.setId(1L);
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndicateurMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        indicateur.setNom(null);

        // Create the Indicateur, which fails.
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        restIndicateurMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        indicateur.setValeur(null);

        // Create the Indicateur, which fails.
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        restIndicateurMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIndicateurs() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        // Get all the indicateurList
        restIndicateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indicateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())));
    }

    @Test
    @Transactional
    void getIndicateur() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        // Get the indicateur
        restIndicateurMockMvc
            .perform(get(ENTITY_API_URL_ID, indicateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indicateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingIndicateur() throws Exception {
        // Get the indicateur
        restIndicateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndicateur() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indicateur
        Indicateur updatedIndicateur = indicateurRepository.findById(indicateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndicateur are not directly saved in db
        em.detach(updatedIndicateur);
        updatedIndicateur.nom(UPDATED_NOM).valeur(UPDATED_VALEUR);
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(updatedIndicateur);

        restIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indicateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndicateurToMatchAllProperties(updatedIndicateur);
    }

    @Test
    @Transactional
    void putNonExistingIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indicateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indicateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndicateurWithPatch() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indicateur using partial update
        Indicateur partialUpdatedIndicateur = new Indicateur();
        partialUpdatedIndicateur.setId(indicateur.getId());

        partialUpdatedIndicateur.valeur(UPDATED_VALEUR);

        restIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndicateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndicateur))
            )
            .andExpect(status().isOk());

        // Validate the Indicateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndicateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndicateur, indicateur),
            getPersistedIndicateur(indicateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndicateurWithPatch() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indicateur using partial update
        Indicateur partialUpdatedIndicateur = new Indicateur();
        partialUpdatedIndicateur.setId(indicateur.getId());

        partialUpdatedIndicateur.nom(UPDATED_NOM).valeur(UPDATED_VALEUR);

        restIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndicateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndicateur))
            )
            .andExpect(status().isOk());

        // Validate the Indicateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndicateurUpdatableFieldsEquals(partialUpdatedIndicateur, getPersistedIndicateur(partialUpdatedIndicateur));
    }

    @Test
    @Transactional
    void patchNonExistingIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indicateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndicateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indicateur.setId(longCount.incrementAndGet());

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndicateurMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(indicateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Indicateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndicateur() throws Exception {
        // Initialize the database
        insertedIndicateur = indicateurRepository.saveAndFlush(indicateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indicateur
        restIndicateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, indicateur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indicateurRepository.count();
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

    protected Indicateur getPersistedIndicateur(Indicateur indicateur) {
        return indicateurRepository.findById(indicateur.getId()).orElseThrow();
    }

    protected void assertPersistedIndicateurToMatchAllProperties(Indicateur expectedIndicateur) {
        assertIndicateurAllPropertiesEquals(expectedIndicateur, getPersistedIndicateur(expectedIndicateur));
    }

    protected void assertPersistedIndicateurToMatchUpdatableProperties(Indicateur expectedIndicateur) {
        assertIndicateurAllUpdatablePropertiesEquals(expectedIndicateur, getPersistedIndicateur(expectedIndicateur));
    }
}
