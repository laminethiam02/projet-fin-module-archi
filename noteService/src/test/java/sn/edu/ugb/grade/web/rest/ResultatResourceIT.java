package sn.edu.ugb.grade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.grade.domain.ResultatAsserts.*;
import static sn.edu.ugb.grade.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.grade.IntegrationTest;
import sn.edu.ugb.grade.domain.Resultat;
import sn.edu.ugb.grade.repository.ResultatRepository;
import sn.edu.ugb.grade.service.dto.ResultatDTO;
import sn.edu.ugb.grade.service.mapper.ResultatMapper;

/**
 * Integration tests for the {@link ResultatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultatResourceIT {

    private static final Float DEFAULT_NOTE_OBTENUE = 1F;
    private static final Float UPDATED_NOTE_OBTENUE = 2F;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Long DEFAULT_EXAMEN_ID = 1L;
    private static final Long UPDATED_EXAMEN_ID = 2L;

    private static final Long DEFAULT_DOSSIER_ID = 1L;
    private static final Long UPDATED_DOSSIER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/resultats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private ResultatMapper resultatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultatMockMvc;

    private Resultat resultat;

    private Resultat insertedResultat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createEntity() {
        return new Resultat()
            .noteObtenue(DEFAULT_NOTE_OBTENUE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .examenId(DEFAULT_EXAMEN_ID)
            .dossierId(DEFAULT_DOSSIER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createUpdatedEntity() {
        return new Resultat()
            .noteObtenue(UPDATED_NOTE_OBTENUE)
            .commentaire(UPDATED_COMMENTAIRE)
            .examenId(UPDATED_EXAMEN_ID)
            .dossierId(UPDATED_DOSSIER_ID);
    }

    @BeforeEach
    void initTest() {
        resultat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedResultat != null) {
            resultatRepository.delete(insertedResultat);
            insertedResultat = null;
        }
    }

    @Test
    @Transactional
    void createResultat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);
        var returnedResultatDTO = om.readValue(
            restResultatMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResultatDTO.class
        );

        // Validate the Resultat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResultat = resultatMapper.toEntity(returnedResultatDTO);
        assertResultatUpdatableFieldsEquals(returnedResultat, getPersistedResultat(returnedResultat));

        insertedResultat = returnedResultat;
    }

    @Test
    @Transactional
    void createResultatWithExistingId() throws Exception {
        // Create the Resultat with an existing ID
        resultat.setId(1L);
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoteObtenueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setNoteObtenue(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExamenIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setExamenId(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDossierIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setDossierId(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResultats() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        // Get all the resultatList
        restResultatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultat.getId().intValue())))
            .andExpect(jsonPath("$.[*].noteObtenue").value(hasItem(DEFAULT_NOTE_OBTENUE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].examenId").value(hasItem(DEFAULT_EXAMEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].dossierId").value(hasItem(DEFAULT_DOSSIER_ID.intValue())));
    }

    @Test
    @Transactional
    void getResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        // Get the resultat
        restResultatMockMvc
            .perform(get(ENTITY_API_URL_ID, resultat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultat.getId().intValue()))
            .andExpect(jsonPath("$.noteObtenue").value(DEFAULT_NOTE_OBTENUE.doubleValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.examenId").value(DEFAULT_EXAMEN_ID.intValue()))
            .andExpect(jsonPath("$.dossierId").value(DEFAULT_DOSSIER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingResultat() throws Exception {
        // Get the resultat
        restResultatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat
        Resultat updatedResultat = resultatRepository.findById(resultat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResultat are not directly saved in db
        em.detach(updatedResultat);
        updatedResultat
            .noteObtenue(UPDATED_NOTE_OBTENUE)
            .commentaire(UPDATED_COMMENTAIRE)
            .examenId(UPDATED_EXAMEN_ID)
            .dossierId(UPDATED_DOSSIER_ID);
        ResultatDTO resultatDTO = resultatMapper.toDto(updatedResultat);

        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResultatToMatchAllProperties(updatedResultat);
    }

    @Test
    @Transactional
    void putNonExistingResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        partialUpdatedResultat.noteObtenue(UPDATED_NOTE_OBTENUE).commentaire(UPDATED_COMMENTAIRE).dossierId(UPDATED_DOSSIER_ID);

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResultatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResultat, resultat), getPersistedResultat(resultat));
    }

    @Test
    @Transactional
    void fullUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        partialUpdatedResultat
            .noteObtenue(UPDATED_NOTE_OBTENUE)
            .commentaire(UPDATED_COMMENTAIRE)
            .examenId(UPDATED_EXAMEN_ID)
            .dossierId(UPDATED_DOSSIER_ID);

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResultatUpdatableFieldsEquals(partialUpdatedResultat, getPersistedResultat(partialUpdatedResultat));
    }

    @Test
    @Transactional
    void patchNonExistingResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultatDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resultat
        restResultatMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resultatRepository.count();
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

    protected Resultat getPersistedResultat(Resultat resultat) {
        return resultatRepository.findById(resultat.getId()).orElseThrow();
    }

    protected void assertPersistedResultatToMatchAllProperties(Resultat expectedResultat) {
        assertResultatAllPropertiesEquals(expectedResultat, getPersistedResultat(expectedResultat));
    }

    protected void assertPersistedResultatToMatchUpdatableProperties(Resultat expectedResultat) {
        assertResultatAllUpdatablePropertiesEquals(expectedResultat, getPersistedResultat(expectedResultat));
    }
}
