package sn.edu.ugb.student.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.student.domain.BulletinAsserts.*;
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
import sn.edu.ugb.student.domain.Bulletin;
import sn.edu.ugb.student.repository.BulletinRepository;
import sn.edu.ugb.student.service.dto.BulletinDTO;
import sn.edu.ugb.student.service.mapper.BulletinMapper;

/**
 * Integration tests for the {@link BulletinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BulletinResourceIT {

    private static final String DEFAULT_ANNEE_ACADEMIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_ACADEMIQUE = "BBBBBBBBBB";

    private static final Float DEFAULT_MOYENNE = 1F;
    private static final Float UPDATED_MOYENNE = 2F;

    private static final String DEFAULT_MENTION = "AAAAAAAAAA";
    private static final String UPDATED_MENTION = "BBBBBBBBBB";

    private static final Long DEFAULT_DOSSIER_ID = 1L;
    private static final Long UPDATED_DOSSIER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/bulletins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BulletinRepository bulletinRepository;

    @Autowired
    private BulletinMapper bulletinMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBulletinMockMvc;

    private Bulletin bulletin;

    private Bulletin insertedBulletin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bulletin createEntity() {
        return new Bulletin()
            .anneeAcademique(DEFAULT_ANNEE_ACADEMIQUE)
            .moyenne(DEFAULT_MOYENNE)
            .mention(DEFAULT_MENTION)
            .dossierId(DEFAULT_DOSSIER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bulletin createUpdatedEntity() {
        return new Bulletin()
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .moyenne(UPDATED_MOYENNE)
            .mention(UPDATED_MENTION)
            .dossierId(UPDATED_DOSSIER_ID);
    }

    @BeforeEach
    void initTest() {
        bulletin = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBulletin != null) {
            bulletinRepository.delete(insertedBulletin);
            insertedBulletin = null;
        }
    }

    @Test
    @Transactional
    void createBulletin() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);
        var returnedBulletinDTO = om.readValue(
            restBulletinMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bulletinDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BulletinDTO.class
        );

        // Validate the Bulletin in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBulletin = bulletinMapper.toEntity(returnedBulletinDTO);
        assertBulletinUpdatableFieldsEquals(returnedBulletin, getPersistedBulletin(returnedBulletin));

        insertedBulletin = returnedBulletin;
    }

    @Test
    @Transactional
    void createBulletinWithExistingId() throws Exception {
        // Create the Bulletin with an existing ID
        bulletin.setId(1L);
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnneeAcademiqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bulletin.setAnneeAcademique(null);

        // Create the Bulletin, which fails.
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDossierIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bulletin.setDossierId(null);

        // Create the Bulletin, which fails.
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        restBulletinMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bulletinDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBulletins() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        // Get all the bulletinList
        restBulletinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulletin.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeAcademique").value(hasItem(DEFAULT_ANNEE_ACADEMIQUE)))
            .andExpect(jsonPath("$.[*].moyenne").value(hasItem(DEFAULT_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION)))
            .andExpect(jsonPath("$.[*].dossierId").value(hasItem(DEFAULT_DOSSIER_ID.intValue())));
    }

    @Test
    @Transactional
    void getBulletin() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        // Get the bulletin
        restBulletinMockMvc
            .perform(get(ENTITY_API_URL_ID, bulletin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bulletin.getId().intValue()))
            .andExpect(jsonPath("$.anneeAcademique").value(DEFAULT_ANNEE_ACADEMIQUE))
            .andExpect(jsonPath("$.moyenne").value(DEFAULT_MOYENNE.doubleValue()))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION))
            .andExpect(jsonPath("$.dossierId").value(DEFAULT_DOSSIER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBulletin() throws Exception {
        // Get the bulletin
        restBulletinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBulletin() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bulletin
        Bulletin updatedBulletin = bulletinRepository.findById(bulletin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBulletin are not directly saved in db
        em.detach(updatedBulletin);
        updatedBulletin
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .moyenne(UPDATED_MOYENNE)
            .mention(UPDATED_MENTION)
            .dossierId(UPDATED_DOSSIER_ID);
        BulletinDTO bulletinDTO = bulletinMapper.toDto(updatedBulletin);

        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBulletinToMatchAllProperties(updatedBulletin);
    }

    @Test
    @Transactional
    void putNonExistingBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bulletinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBulletinWithPatch() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bulletin using partial update
        Bulletin partialUpdatedBulletin = new Bulletin();
        partialUpdatedBulletin.setId(bulletin.getId());

        partialUpdatedBulletin
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .moyenne(UPDATED_MOYENNE)
            .mention(UPDATED_MENTION)
            .dossierId(UPDATED_DOSSIER_ID);

        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulletin.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBulletin))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBulletinUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBulletin, bulletin), getPersistedBulletin(bulletin));
    }

    @Test
    @Transactional
    void fullUpdateBulletinWithPatch() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bulletin using partial update
        Bulletin partialUpdatedBulletin = new Bulletin();
        partialUpdatedBulletin.setId(bulletin.getId());

        partialUpdatedBulletin
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .moyenne(UPDATED_MOYENNE)
            .mention(UPDATED_MENTION)
            .dossierId(UPDATED_DOSSIER_ID);

        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulletin.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBulletin))
            )
            .andExpect(status().isOk());

        // Validate the Bulletin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBulletinUpdatableFieldsEquals(partialUpdatedBulletin, getPersistedBulletin(partialUpdatedBulletin));
    }

    @Test
    @Transactional
    void patchNonExistingBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bulletinDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBulletin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bulletin.setId(longCount.incrementAndGet());

        // Create the Bulletin
        BulletinDTO bulletinDTO = bulletinMapper.toDto(bulletin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulletinMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bulletinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bulletin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBulletin() throws Exception {
        // Initialize the database
        insertedBulletin = bulletinRepository.saveAndFlush(bulletin);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bulletin
        restBulletinMockMvc
            .perform(delete(ENTITY_API_URL_ID, bulletin.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bulletinRepository.count();
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

    protected Bulletin getPersistedBulletin(Bulletin bulletin) {
        return bulletinRepository.findById(bulletin.getId()).orElseThrow();
    }

    protected void assertPersistedBulletinToMatchAllProperties(Bulletin expectedBulletin) {
        assertBulletinAllPropertiesEquals(expectedBulletin, getPersistedBulletin(expectedBulletin));
    }

    protected void assertPersistedBulletinToMatchUpdatableProperties(Bulletin expectedBulletin) {
        assertBulletinAllUpdatablePropertiesEquals(expectedBulletin, getPersistedBulletin(expectedBulletin));
    }
}
