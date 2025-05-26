package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.ProgrammeAsserts.*;
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
import sn.edu.ugb.curriculum.domain.Programme;
import sn.edu.ugb.curriculum.repository.ProgrammeRepository;
import sn.edu.ugb.curriculum.service.dto.ProgrammeDTO;
import sn.edu.ugb.curriculum.service.mapper.ProgrammeMapper;

/**
 * Integration tests for the {@link ProgrammeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgrammeResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_PROGRAMME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PROGRAMME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/programmes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgrammeMockMvc;

    private Programme programme;

    private Programme insertedProgramme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createEntity() {
        return new Programme().intitule(DEFAULT_INTITULE).codeProgramme(DEFAULT_CODE_PROGRAMME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createUpdatedEntity() {
        return new Programme().intitule(UPDATED_INTITULE).codeProgramme(UPDATED_CODE_PROGRAMME);
    }

    @BeforeEach
    void initTest() {
        programme = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProgramme != null) {
            programmeRepository.delete(insertedProgramme);
            insertedProgramme = null;
        }
    }

    @Test
    @Transactional
    void createProgramme() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);
        var returnedProgrammeDTO = om.readValue(
            restProgrammeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programmeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProgrammeDTO.class
        );

        // Validate the Programme in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProgramme = programmeMapper.toEntity(returnedProgrammeDTO);
        assertProgrammeUpdatableFieldsEquals(returnedProgramme, getPersistedProgramme(returnedProgramme));

        insertedProgramme = returnedProgramme;
    }

    @Test
    @Transactional
    void createProgrammeWithExistingId() throws Exception {
        // Create the Programme with an existing ID
        programme.setId(1L);
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programmeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        programme.setIntitule(null);

        // Create the Programme, which fails.
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programmeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeProgrammeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        programme.setCodeProgramme(null);

        // Create the Programme, which fails.
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programmeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProgrammes() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        // Get all the programmeList
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].codeProgramme").value(hasItem(DEFAULT_CODE_PROGRAMME)));
    }

    @Test
    @Transactional
    void getProgramme() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        // Get the programme
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, programme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programme.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.codeProgramme").value(DEFAULT_CODE_PROGRAMME));
    }

    @Test
    @Transactional
    void getNonExistingProgramme() throws Exception {
        // Get the programme
        restProgrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgramme() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programme
        Programme updatedProgramme = programmeRepository.findById(programme.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProgramme are not directly saved in db
        em.detach(updatedProgramme);
        updatedProgramme.intitule(UPDATED_INTITULE).codeProgramme(UPDATED_CODE_PROGRAMME);
        ProgrammeDTO programmeDTO = programmeMapper.toDto(updatedProgramme);

        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProgrammeToMatchAllProperties(updatedProgramme);
    }

    @Test
    @Transactional
    void putNonExistingProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programmeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme.codeProgramme(UPDATED_CODE_PROGRAMME);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgrammeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProgramme, programme),
            getPersistedProgramme(programme)
        );
    }

    @Test
    @Transactional
    void fullUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme.intitule(UPDATED_INTITULE).codeProgramme(UPDATED_CODE_PROGRAMME);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgrammeUpdatableFieldsEquals(partialUpdatedProgramme, getPersistedProgramme(partialUpdatedProgramme));
    }

    @Test
    @Transactional
    void patchNonExistingProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programmeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgramme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programme.setId(longCount.incrementAndGet());

        // Create the Programme
        ProgrammeDTO programmeDTO = programmeMapper.toDto(programme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(programmeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgramme() throws Exception {
        // Initialize the database
        insertedProgramme = programmeRepository.saveAndFlush(programme);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the programme
        restProgrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, programme.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return programmeRepository.count();
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

    protected Programme getPersistedProgramme(Programme programme) {
        return programmeRepository.findById(programme.getId()).orElseThrow();
    }

    protected void assertPersistedProgrammeToMatchAllProperties(Programme expectedProgramme) {
        assertProgrammeAllPropertiesEquals(expectedProgramme, getPersistedProgramme(expectedProgramme));
    }

    protected void assertPersistedProgrammeToMatchUpdatableProperties(Programme expectedProgramme) {
        assertProgrammeAllUpdatablePropertiesEquals(expectedProgramme, getPersistedProgramme(expectedProgramme));
    }
}
