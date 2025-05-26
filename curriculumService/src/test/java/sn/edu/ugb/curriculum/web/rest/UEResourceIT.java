package sn.edu.ugb.curriculum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.curriculum.domain.UEAsserts.*;
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
import sn.edu.ugb.curriculum.domain.UE;
import sn.edu.ugb.curriculum.repository.UERepository;
import sn.edu.ugb.curriculum.service.dto.UEDTO;
import sn.edu.ugb.curriculum.service.mapper.UEMapper;

/**
 * Integration tests for the {@link UEResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UEResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDITS = 1;
    private static final Integer UPDATED_CREDITS = 2;

    private static final Long DEFAULT_NIVEAU_ID = 1L;
    private static final Long UPDATED_NIVEAU_ID = 2L;

    private static final String ENTITY_API_URL = "/api/ues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UERepository uERepository;

    @Autowired
    private UEMapper uEMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUEMockMvc;

    private UE uE;

    private UE insertedUE;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UE createEntity() {
        return new UE().intitule(DEFAULT_INTITULE).credits(DEFAULT_CREDITS).niveauId(DEFAULT_NIVEAU_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UE createUpdatedEntity() {
        return new UE().intitule(UPDATED_INTITULE).credits(UPDATED_CREDITS).niveauId(UPDATED_NIVEAU_ID);
    }

    @BeforeEach
    void initTest() {
        uE = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUE != null) {
            uERepository.delete(insertedUE);
            insertedUE = null;
        }
    }

    @Test
    @Transactional
    void createUE() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);
        var returnedUEDTO = om.readValue(
            restUEMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UEDTO.class
        );

        // Validate the UE in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUE = uEMapper.toEntity(returnedUEDTO);
        assertUEUpdatableFieldsEquals(returnedUE, getPersistedUE(returnedUE));

        insertedUE = returnedUE;
    }

    @Test
    @Transactional
    void createUEWithExistingId() throws Exception {
        // Create the UE with an existing ID
        uE.setId(1L);
        UEDTO uEDTO = uEMapper.toDto(uE);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUEMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uE.setIntitule(null);

        // Create the UE, which fails.
        UEDTO uEDTO = uEMapper.toDto(uE);

        restUEMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uE.setCredits(null);

        // Create the UE, which fails.
        UEDTO uEDTO = uEMapper.toDto(uE);

        restUEMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uE.setNiveauId(null);

        // Create the UE, which fails.
        UEDTO uEDTO = uEMapper.toDto(uE);

        restUEMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUES() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        // Get all the uEList
        restUEMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uE.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)))
            .andExpect(jsonPath("$.[*].niveauId").value(hasItem(DEFAULT_NIVEAU_ID.intValue())));
    }

    @Test
    @Transactional
    void getUE() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        // Get the uE
        restUEMockMvc
            .perform(get(ENTITY_API_URL_ID, uE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uE.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS))
            .andExpect(jsonPath("$.niveauId").value(DEFAULT_NIVEAU_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUE() throws Exception {
        // Get the uE
        restUEMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUE() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uE
        UE updatedUE = uERepository.findById(uE.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUE are not directly saved in db
        em.detach(updatedUE);
        updatedUE.intitule(UPDATED_INTITULE).credits(UPDATED_CREDITS).niveauId(UPDATED_NIVEAU_ID);
        UEDTO uEDTO = uEMapper.toDto(updatedUE);

        restUEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uEDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uEDTO))
            )
            .andExpect(status().isOk());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUEToMatchAllProperties(updatedUE);
    }

    @Test
    @Transactional
    void putNonExistingUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uEDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUEWithPatch() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uE using partial update
        UE partialUpdatedUE = new UE();
        partialUpdatedUE.setId(uE.getId());

        restUEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUE.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUE))
            )
            .andExpect(status().isOk());

        // Validate the UE in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUEUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUE, uE), getPersistedUE(uE));
    }

    @Test
    @Transactional
    void fullUpdateUEWithPatch() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uE using partial update
        UE partialUpdatedUE = new UE();
        partialUpdatedUE.setId(uE.getId());

        partialUpdatedUE.intitule(UPDATED_INTITULE).credits(UPDATED_CREDITS).niveauId(UPDATED_NIVEAU_ID);

        restUEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUE.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUE))
            )
            .andExpect(status().isOk());

        // Validate the UE in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUEUpdatableFieldsEquals(partialUpdatedUE, getPersistedUE(partialUpdatedUE));
    }

    @Test
    @Transactional
    void patchNonExistingUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uEDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uEDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uE.setId(longCount.incrementAndGet());

        // Create the UE
        UEDTO uEDTO = uEMapper.toDto(uE);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUEMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uEDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUE() throws Exception {
        // Initialize the database
        insertedUE = uERepository.saveAndFlush(uE);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uE
        restUEMockMvc
            .perform(delete(ENTITY_API_URL_ID, uE.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uERepository.count();
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

    protected UE getPersistedUE(UE uE) {
        return uERepository.findById(uE.getId()).orElseThrow();
    }

    protected void assertPersistedUEToMatchAllProperties(UE expectedUE) {
        assertUEAllPropertiesEquals(expectedUE, getPersistedUE(expectedUE));
    }

    protected void assertPersistedUEToMatchUpdatableProperties(UE expectedUE) {
        assertUEAllUpdatablePropertiesEquals(expectedUE, getPersistedUE(expectedUE));
    }
}
