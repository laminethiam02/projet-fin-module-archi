package sn.edu.ugb.teacher.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.edu.ugb.teacher.domain.ContratAsserts.*;
import static sn.edu.ugb.teacher.web.rest.TestUtil.createUpdateProxyForBean;

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
import sn.edu.ugb.teacher.IntegrationTest;
import sn.edu.ugb.teacher.domain.Contrat;
import sn.edu.ugb.teacher.repository.ContratRepository;
import sn.edu.ugb.teacher.service.dto.ContratDTO;
import sn.edu.ugb.teacher.service.mapper.ContratMapper;

/**
 * Integration tests for the {@link ContratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratResourceIT {

    private static final String DEFAULT_TYPE_CONTRAT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CONTRAT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PROFIL_ID = 1L;
    private static final Long UPDATED_PROFIL_ID = 2L;

    private static final String ENTITY_API_URL = "/api/contrats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private ContratMapper contratMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratMockMvc;

    private Contrat contrat;

    private Contrat insertedContrat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrat createEntity() {
        return new Contrat()
            .typeContrat(DEFAULT_TYPE_CONTRAT)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .profilId(DEFAULT_PROFIL_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrat createUpdatedEntity() {
        return new Contrat()
            .typeContrat(UPDATED_TYPE_CONTRAT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .profilId(UPDATED_PROFIL_ID);
    }

    @BeforeEach
    void initTest() {
        contrat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContrat != null) {
            contratRepository.delete(insertedContrat);
            insertedContrat = null;
        }
    }

    @Test
    @Transactional
    void createContrat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);
        var returnedContratDTO = om.readValue(
            restContratMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContratDTO.class
        );

        // Validate the Contrat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContrat = contratMapper.toEntity(returnedContratDTO);
        assertContratUpdatableFieldsEquals(returnedContrat, getPersistedContrat(returnedContrat));

        insertedContrat = returnedContrat;
    }

    @Test
    @Transactional
    void createContratWithExistingId() throws Exception {
        // Create the Contrat with an existing ID
        contrat.setId(1L);
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeContratIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contrat.setTypeContrat(null);

        // Create the Contrat, which fails.
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        restContratMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contrat.setDateDebut(null);

        // Create the Contrat, which fails.
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        restContratMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfilIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contrat.setProfilId(null);

        // Create the Contrat, which fails.
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        restContratMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContrats() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        // Get all the contratList
        restContratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrat.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeContrat").value(hasItem(DEFAULT_TYPE_CONTRAT)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].profilId").value(hasItem(DEFAULT_PROFIL_ID.intValue())));
    }

    @Test
    @Transactional
    void getContrat() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        // Get the contrat
        restContratMockMvc
            .perform(get(ENTITY_API_URL_ID, contrat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contrat.getId().intValue()))
            .andExpect(jsonPath("$.typeContrat").value(DEFAULT_TYPE_CONTRAT))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.profilId").value(DEFAULT_PROFIL_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingContrat() throws Exception {
        // Get the contrat
        restContratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContrat() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrat
        Contrat updatedContrat = contratRepository.findById(contrat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContrat are not directly saved in db
        em.detach(updatedContrat);
        updatedContrat
            .typeContrat(UPDATED_TYPE_CONTRAT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .profilId(UPDATED_PROFIL_ID);
        ContratDTO contratDTO = contratMapper.toDto(updatedContrat);

        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContratToMatchAllProperties(updatedContrat);
    }

    @Test
    @Transactional
    void putNonExistingContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contratDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratWithPatch() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrat using partial update
        Contrat partialUpdatedContrat = new Contrat();
        partialUpdatedContrat.setId(contrat.getId());

        partialUpdatedContrat.dateFin(UPDATED_DATE_FIN).profilId(UPDATED_PROFIL_ID);

        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContrat))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContratUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContrat, contrat), getPersistedContrat(contrat));
    }

    @Test
    @Transactional
    void fullUpdateContratWithPatch() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contrat using partial update
        Contrat partialUpdatedContrat = new Contrat();
        partialUpdatedContrat.setId(contrat.getId());

        partialUpdatedContrat
            .typeContrat(UPDATED_TYPE_CONTRAT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .profilId(UPDATED_PROFIL_ID);

        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContrat))
            )
            .andExpect(status().isOk());

        // Validate the Contrat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContratUpdatableFieldsEquals(partialUpdatedContrat, getPersistedContrat(partialUpdatedContrat));
    }

    @Test
    @Transactional
    void patchNonExistingContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contratDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContrat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contrat.setId(longCount.incrementAndGet());

        // Create the Contrat
        ContratDTO contratDTO = contratMapper.toDto(contrat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContrat() throws Exception {
        // Initialize the database
        insertedContrat = contratRepository.saveAndFlush(contrat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contrat
        restContratMockMvc
            .perform(delete(ENTITY_API_URL_ID, contrat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contratRepository.count();
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

    protected Contrat getPersistedContrat(Contrat contrat) {
        return contratRepository.findById(contrat.getId()).orElseThrow();
    }

    protected void assertPersistedContratToMatchAllProperties(Contrat expectedContrat) {
        assertContratAllPropertiesEquals(expectedContrat, getPersistedContrat(expectedContrat));
    }

    protected void assertPersistedContratToMatchUpdatableProperties(Contrat expectedContrat) {
        assertContratAllUpdatablePropertiesEquals(expectedContrat, getPersistedContrat(expectedContrat));
    }
}
