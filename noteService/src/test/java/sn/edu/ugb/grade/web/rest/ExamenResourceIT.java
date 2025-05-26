package sn.edu.ugb.grade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.grade.domain.ExamenAsserts.*;
import static sn.edu.ugb.grade.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.grade.IntegrationTest;
import sn.edu.ugb.grade.domain.Examen;
import sn.edu.ugb.grade.repository.ExamenRepository;
import sn.edu.ugb.grade.service.dto.ExamenDTO;
import sn.edu.ugb.grade.service.mapper.ExamenMapper;

/**
 * Integration tests for the {@link ExamenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamenResourceIT {

    private static final String DEFAULT_TYPE_EXAMEN = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_EXAMEN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EXAMEN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXAMEN = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_MATIERE_ID = 1L;
    private static final Long UPDATED_MATIERE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/examen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private ExamenMapper examenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamenMockMvc;

    private Examen examen;

    private Examen insertedExamen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Examen createEntity() {
        return new Examen().typeExamen(DEFAULT_TYPE_EXAMEN).dateExamen(DEFAULT_DATE_EXAMEN).matiereId(DEFAULT_MATIERE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Examen createUpdatedEntity() {
        return new Examen().typeExamen(UPDATED_TYPE_EXAMEN).dateExamen(UPDATED_DATE_EXAMEN).matiereId(UPDATED_MATIERE_ID);
    }

    @BeforeEach
    void initTest() {
        examen = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExamen != null) {
            examenRepository.delete(insertedExamen);
            insertedExamen = null;
        }
    }

    @Test
    @Transactional
    void createExamen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);
        var returnedExamenDTO = om.readValue(
            restExamenMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExamenDTO.class
        );

        // Validate the Examen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExamen = examenMapper.toEntity(returnedExamenDTO);
        assertExamenUpdatableFieldsEquals(returnedExamen, getPersistedExamen(returnedExamen));

        insertedExamen = returnedExamen;
    }

    @Test
    @Transactional
    void createExamenWithExistingId() throws Exception {
        // Create the Examen with an existing ID
        examen.setId(1L);
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamenMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeExamenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examen.setTypeExamen(null);

        // Create the Examen, which fails.
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        restExamenMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateExamenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examen.setDateExamen(null);

        // Create the Examen, which fails.
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        restExamenMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatiereIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        examen.setMatiereId(null);

        // Create the Examen, which fails.
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        restExamenMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamen() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        // Get all the examenList
        restExamenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examen.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeExamen").value(hasItem(DEFAULT_TYPE_EXAMEN)))
            .andExpect(jsonPath("$.[*].dateExamen").value(hasItem(DEFAULT_DATE_EXAMEN.toString())))
            .andExpect(jsonPath("$.[*].matiereId").value(hasItem(DEFAULT_MATIERE_ID.intValue())));
    }

    @Test
    @Transactional
    void getExamen() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        // Get the examen
        restExamenMockMvc
            .perform(get(ENTITY_API_URL_ID, examen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examen.getId().intValue()))
            .andExpect(jsonPath("$.typeExamen").value(DEFAULT_TYPE_EXAMEN))
            .andExpect(jsonPath("$.dateExamen").value(DEFAULT_DATE_EXAMEN.toString()))
            .andExpect(jsonPath("$.matiereId").value(DEFAULT_MATIERE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExamen() throws Exception {
        // Get the examen
        restExamenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamen() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examen
        Examen updatedExamen = examenRepository.findById(examen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExamen are not directly saved in db
        em.detach(updatedExamen);
        updatedExamen.typeExamen(UPDATED_TYPE_EXAMEN).dateExamen(UPDATED_DATE_EXAMEN).matiereId(UPDATED_MATIERE_ID);
        ExamenDTO examenDTO = examenMapper.toDto(updatedExamen);

        restExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examenDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExamenToMatchAllProperties(updatedExamen);
    }

    @Test
    @Transactional
    void putNonExistingExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examenDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(examenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamenWithPatch() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examen using partial update
        Examen partialUpdatedExamen = new Examen();
        partialUpdatedExamen.setId(examen.getId());

        restExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamen.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamen))
            )
            .andExpect(status().isOk());

        // Validate the Examen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamenUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExamen, examen), getPersistedExamen(examen));
    }

    @Test
    @Transactional
    void fullUpdateExamenWithPatch() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the examen using partial update
        Examen partialUpdatedExamen = new Examen();
        partialUpdatedExamen.setId(examen.getId());

        partialUpdatedExamen.typeExamen(UPDATED_TYPE_EXAMEN).dateExamen(UPDATED_DATE_EXAMEN).matiereId(UPDATED_MATIERE_ID);

        restExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamen.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExamen))
            )
            .andExpect(status().isOk());

        // Validate the Examen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExamenUpdatableFieldsEquals(partialUpdatedExamen, getPersistedExamen(partialUpdatedExamen));
    }

    @Test
    @Transactional
    void patchNonExistingExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examenDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        examen.setId(longCount.incrementAndGet());

        // Create the Examen
        ExamenDTO examenDTO = examenMapper.toDto(examen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamenMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(examenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Examen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamen() throws Exception {
        // Initialize the database
        insertedExamen = examenRepository.saveAndFlush(examen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the examen
        restExamenMockMvc
            .perform(delete(ENTITY_API_URL_ID, examen.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return examenRepository.count();
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

    protected Examen getPersistedExamen(Examen examen) {
        return examenRepository.findById(examen.getId()).orElseThrow();
    }

    protected void assertPersistedExamenToMatchAllProperties(Examen expectedExamen) {
        assertExamenAllPropertiesEquals(expectedExamen, getPersistedExamen(expectedExamen));
    }

    protected void assertPersistedExamenToMatchUpdatableProperties(Examen expectedExamen) {
        assertExamenAllUpdatablePropertiesEquals(expectedExamen, getPersistedExamen(expectedExamen));
    }
}
