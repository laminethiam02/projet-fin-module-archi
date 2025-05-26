package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.ReleveNoteAsserts.*;
import static sn.edu.ugb.student.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.student.IntegrationTest;
import sn.edu.ugb.student.domain.ReleveNote;
import sn.edu.ugb.student.repository.ReleveNoteRepository;
import sn.edu.ugb.student.service.dto.ReleveNoteDTO;
import sn.edu.ugb.student.service.mapper.ReleveNoteMapper;

/**
 * Integration tests for the {@link ReleveNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReleveNoteResourceIT {

    private static final String DEFAULT_SEMESTRE = "AAAAAAAAAA";
    private static final String UPDATED_SEMESTRE = "BBBBBBBBBB";

    private static final Float DEFAULT_NOTE_GLOBALE = 1F;
    private static final Float UPDATED_NOTE_GLOBALE = 2F;

    private static final Long DEFAULT_DOSSIER_ID = 1L;
    private static final Long UPDATED_DOSSIER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/releve-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReleveNoteRepository releveNoteRepository;

    @Autowired
    private ReleveNoteMapper releveNoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleveNoteMockMvc;

    private ReleveNote releveNote;

    private ReleveNote insertedReleveNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveNote createEntity() {
        return new ReleveNote().semestre(DEFAULT_SEMESTRE).noteGlobale(DEFAULT_NOTE_GLOBALE).dossierId(DEFAULT_DOSSIER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleveNote createUpdatedEntity() {
        return new ReleveNote().semestre(UPDATED_SEMESTRE).noteGlobale(UPDATED_NOTE_GLOBALE).dossierId(UPDATED_DOSSIER_ID);
    }

    @BeforeEach
    void initTest() {
        releveNote = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReleveNote != null) {
            releveNoteRepository.delete(insertedReleveNote);
            insertedReleveNote = null;
        }
    }

    @Test
    @Transactional
    void createReleveNote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);
        var returnedReleveNoteDTO = om.readValue(
            restReleveNoteMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReleveNoteDTO.class
        );

        // Validate the ReleveNote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReleveNote = releveNoteMapper.toEntity(returnedReleveNoteDTO);
        assertReleveNoteUpdatableFieldsEquals(returnedReleveNote, getPersistedReleveNote(returnedReleveNote));

        insertedReleveNote = returnedReleveNote;
    }

    @Test
    @Transactional
    void createReleveNoteWithExistingId() throws Exception {
        // Create the ReleveNote with an existing ID
        releveNote.setId(1L);
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSemestreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        releveNote.setSemestre(null);

        // Create the ReleveNote, which fails.
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteGlobaleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        releveNote.setNoteGlobale(null);

        // Create the ReleveNote, which fails.
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDossierIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        releveNote.setDossierId(null);

        // Create the ReleveNote, which fails.
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        restReleveNoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReleveNotes() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        // Get all the releveNoteList
        restReleveNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releveNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE)))
            .andExpect(jsonPath("$.[*].noteGlobale").value(hasItem(DEFAULT_NOTE_GLOBALE.doubleValue())))
            .andExpect(jsonPath("$.[*].dossierId").value(hasItem(DEFAULT_DOSSIER_ID.intValue())));
    }

    @Test
    @Transactional
    void getReleveNote() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        // Get the releveNote
        restReleveNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, releveNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releveNote.getId().intValue()))
            .andExpect(jsonPath("$.semestre").value(DEFAULT_SEMESTRE))
            .andExpect(jsonPath("$.noteGlobale").value(DEFAULT_NOTE_GLOBALE.doubleValue()))
            .andExpect(jsonPath("$.dossierId").value(DEFAULT_DOSSIER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingReleveNote() throws Exception {
        // Get the releveNote
        restReleveNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReleveNote() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the releveNote
        ReleveNote updatedReleveNote = releveNoteRepository.findById(releveNote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReleveNote are not directly saved in db
        em.detach(updatedReleveNote);
        updatedReleveNote.semestre(UPDATED_SEMESTRE).noteGlobale(UPDATED_NOTE_GLOBALE).dossierId(UPDATED_DOSSIER_ID);
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(updatedReleveNote);

        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releveNoteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReleveNoteToMatchAllProperties(updatedReleveNote);
    }

    @Test
    @Transactional
    void putNonExistingReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releveNoteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(releveNoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleveNoteWithPatch() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the releveNote using partial update
        ReleveNote partialUpdatedReleveNote = new ReleveNote();
        partialUpdatedReleveNote.setId(releveNote.getId());

        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveNote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReleveNote))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReleveNoteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReleveNote, releveNote),
            getPersistedReleveNote(releveNote)
        );
    }

    @Test
    @Transactional
    void fullUpdateReleveNoteWithPatch() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the releveNote using partial update
        ReleveNote partialUpdatedReleveNote = new ReleveNote();
        partialUpdatedReleveNote.setId(releveNote.getId());

        partialUpdatedReleveNote.semestre(UPDATED_SEMESTRE).noteGlobale(UPDATED_NOTE_GLOBALE).dossierId(UPDATED_DOSSIER_ID);

        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleveNote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReleveNote))
            )
            .andExpect(status().isOk());

        // Validate the ReleveNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReleveNoteUpdatableFieldsEquals(partialUpdatedReleveNote, getPersistedReleveNote(partialUpdatedReleveNote));
    }

    @Test
    @Transactional
    void patchNonExistingReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releveNoteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReleveNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        releveNote.setId(longCount.incrementAndGet());

        // Create the ReleveNote
        ReleveNoteDTO releveNoteDTO = releveNoteMapper.toDto(releveNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleveNoteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(releveNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleveNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReleveNote() throws Exception {
        // Initialize the database
        insertedReleveNote = releveNoteRepository.saveAndFlush(releveNote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the releveNote
        restReleveNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, releveNote.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return releveNoteRepository.count();
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

    protected ReleveNote getPersistedReleveNote(ReleveNote releveNote) {
        return releveNoteRepository.findById(releveNote.getId()).orElseThrow();
    }

    protected void assertPersistedReleveNoteToMatchAllProperties(ReleveNote expectedReleveNote) {
        assertReleveNoteAllPropertiesEquals(expectedReleveNote, getPersistedReleveNote(expectedReleveNote));
    }

    protected void assertPersistedReleveNoteToMatchUpdatableProperties(ReleveNote expectedReleveNote) {
        assertReleveNoteAllUpdatablePropertiesEquals(expectedReleveNote, getPersistedReleveNote(expectedReleveNote));
    }
}
