package sn.edu.ugb.grade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.grade.domain.BaremeAsserts.*;
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
import sn.edu.ugb.grade.domain.Bareme;
import sn.edu.ugb.grade.repository.BaremeRepository;
import sn.edu.ugb.grade.service.dto.BaremeDTO;
import sn.edu.ugb.grade.service.mapper.BaremeMapper;

/**
 * Integration tests for the {@link BaremeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaremeResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_POINTS = 1;
    private static final Integer UPDATED_TOTAL_POINTS = 2;

    private static final Long DEFAULT_EXAMEN_ID = 1L;
    private static final Long UPDATED_EXAMEN_ID = 2L;

    private static final String ENTITY_API_URL = "/api/baremes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BaremeRepository baremeRepository;

    @Autowired
    private BaremeMapper baremeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaremeMockMvc;

    private Bareme bareme;

    private Bareme insertedBareme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bareme createEntity() {
        return new Bareme().intitule(DEFAULT_INTITULE).totalPoints(DEFAULT_TOTAL_POINTS).examenId(DEFAULT_EXAMEN_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bareme createUpdatedEntity() {
        return new Bareme().intitule(UPDATED_INTITULE).totalPoints(UPDATED_TOTAL_POINTS).examenId(UPDATED_EXAMEN_ID);
    }

    @BeforeEach
    void initTest() {
        bareme = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBareme != null) {
            baremeRepository.delete(insertedBareme);
            insertedBareme = null;
        }
    }

    @Test
    @Transactional
    void createBareme() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);
        var returnedBaremeDTO = om.readValue(
            restBaremeMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BaremeDTO.class
        );

        // Validate the Bareme in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBareme = baremeMapper.toEntity(returnedBaremeDTO);
        assertBaremeUpdatableFieldsEquals(returnedBareme, getPersistedBareme(returnedBareme));

        insertedBareme = returnedBareme;
    }

    @Test
    @Transactional
    void createBaremeWithExistingId() throws Exception {
        // Create the Bareme with an existing ID
        bareme.setId(1L);
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaremeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTotalPointsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bareme.setTotalPoints(null);

        // Create the Bareme, which fails.
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        restBaremeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExamenIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bareme.setExamenId(null);

        // Create the Bareme, which fails.
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        restBaremeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBaremes() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        // Get all the baremeList
        restBaremeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bareme.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS)))
            .andExpect(jsonPath("$.[*].examenId").value(hasItem(DEFAULT_EXAMEN_ID.intValue())));
    }

    @Test
    @Transactional
    void getBareme() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        // Get the bareme
        restBaremeMockMvc
            .perform(get(ENTITY_API_URL_ID, bareme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bareme.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.totalPoints").value(DEFAULT_TOTAL_POINTS))
            .andExpect(jsonPath("$.examenId").value(DEFAULT_EXAMEN_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBareme() throws Exception {
        // Get the bareme
        restBaremeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBareme() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bareme
        Bareme updatedBareme = baremeRepository.findById(bareme.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBareme are not directly saved in db
        em.detach(updatedBareme);
        updatedBareme.intitule(UPDATED_INTITULE).totalPoints(UPDATED_TOTAL_POINTS).examenId(UPDATED_EXAMEN_ID);
        BaremeDTO baremeDTO = baremeMapper.toDto(updatedBareme);

        restBaremeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baremeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBaremeToMatchAllProperties(updatedBareme);
    }

    @Test
    @Transactional
    void putNonExistingBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baremeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaremeWithPatch() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bareme using partial update
        Bareme partialUpdatedBareme = new Bareme();
        partialUpdatedBareme.setId(bareme.getId());

        partialUpdatedBareme.intitule(UPDATED_INTITULE);

        restBaremeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBareme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBareme))
            )
            .andExpect(status().isOk());

        // Validate the Bareme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaremeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBareme, bareme), getPersistedBareme(bareme));
    }

    @Test
    @Transactional
    void fullUpdateBaremeWithPatch() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bareme using partial update
        Bareme partialUpdatedBareme = new Bareme();
        partialUpdatedBareme.setId(bareme.getId());

        partialUpdatedBareme.intitule(UPDATED_INTITULE).totalPoints(UPDATED_TOTAL_POINTS).examenId(UPDATED_EXAMEN_ID);

        restBaremeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBareme.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBareme))
            )
            .andExpect(status().isOk());

        // Validate the Bareme in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaremeUpdatableFieldsEquals(partialUpdatedBareme, getPersistedBareme(partialUpdatedBareme));
    }

    @Test
    @Transactional
    void patchNonExistingBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baremeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBareme() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bareme.setId(longCount.incrementAndGet());

        // Create the Bareme
        BaremeDTO baremeDTO = baremeMapper.toDto(bareme);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(baremeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bareme in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBareme() throws Exception {
        // Initialize the database
        insertedBareme = baremeRepository.saveAndFlush(bareme);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bareme
        restBaremeMockMvc
            .perform(delete(ENTITY_API_URL_ID, bareme.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return baremeRepository.count();
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

    protected Bareme getPersistedBareme(Bareme bareme) {
        return baremeRepository.findById(bareme.getId()).orElseThrow();
    }

    protected void assertPersistedBaremeToMatchAllProperties(Bareme expectedBareme) {
        assertBaremeAllPropertiesEquals(expectedBareme, getPersistedBareme(expectedBareme));
    }

    protected void assertPersistedBaremeToMatchUpdatableProperties(Bareme expectedBareme) {
        assertBaremeAllUpdatablePropertiesEquals(expectedBareme, getPersistedBareme(expectedBareme));
    }
}
