package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Partit;
import net.yosef.repository.PartitRepository;
import net.yosef.repository.search.PartitSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PartitResource REST controller.
 *
 * @see PartitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PartitResourceTest {

    private static final String DEFAULT_NOM_V = "SAMPLE_TEXT";
    private static final String UPDATED_NOM_V = "UPDATED_TEXT";
    private static final String DEFAULT_NOM_L = "SAMPLE_TEXT";
    private static final String UPDATED_NOM_L = "UPDATED_TEXT";

    private static final Integer DEFAULT_GOLS_V = 0;
    private static final Integer UPDATED_GOLS_V = 1;

    private static final Integer DEFAULT_GOLS_L = 0;
    private static final Integer UPDATED_GOLS_L = 1;

    private static final LocalDate DEFAULT_DATA = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA = new LocalDate();
    private static final String DEFAULT_ARBITRE = "SAMPLE_TEXT";
    private static final String UPDATED_ARBITRE = "UPDATED_TEXT";

    @Inject
    private PartitRepository partitRepository;

    @Inject
    private PartitSearchRepository partitSearchRepository;

    private MockMvc restPartitMockMvc;

    private Partit partit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartitResource partitResource = new PartitResource();
        ReflectionTestUtils.setField(partitResource, "partitRepository", partitRepository);
        ReflectionTestUtils.setField(partitResource, "partitSearchRepository", partitSearchRepository);
        this.restPartitMockMvc = MockMvcBuilders.standaloneSetup(partitResource).build();
    }

    @Before
    public void initTest() {
        partit = new Partit();
        partit.setNom_v(DEFAULT_NOM_V);
        partit.setNom_l(DEFAULT_NOM_L);
        partit.setGols_v(DEFAULT_GOLS_V);
        partit.setGols_l(DEFAULT_GOLS_L);
        partit.setData(DEFAULT_DATA);
        partit.setArbitre(DEFAULT_ARBITRE);
    }

    @Test
    @Transactional
    public void createPartit() throws Exception {
        int databaseSizeBeforeCreate = partitRepository.findAll().size();

        // Create the Partit
        restPartitMockMvc.perform(post("/api/partits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partit)))
                .andExpect(status().isCreated());

        // Validate the Partit in the database
        List<Partit> partits = partitRepository.findAll();
        assertThat(partits).hasSize(databaseSizeBeforeCreate + 1);
        Partit testPartit = partits.get(partits.size() - 1);
        assertThat(testPartit.getNom_v()).isEqualTo(DEFAULT_NOM_V);
        assertThat(testPartit.getNom_l()).isEqualTo(DEFAULT_NOM_L);
        assertThat(testPartit.getGols_v()).isEqualTo(DEFAULT_GOLS_V);
        assertThat(testPartit.getGols_l()).isEqualTo(DEFAULT_GOLS_L);
        assertThat(testPartit.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testPartit.getArbitre()).isEqualTo(DEFAULT_ARBITRE);
    }

    @Test
    @Transactional
    public void getAllPartits() throws Exception {
        // Initialize the database
        partitRepository.saveAndFlush(partit);

        // Get all the partits
        restPartitMockMvc.perform(get("/api/partits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partit.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom_v").value(hasItem(DEFAULT_NOM_V.toString())))
                .andExpect(jsonPath("$.[*].nom_l").value(hasItem(DEFAULT_NOM_L.toString())))
                .andExpect(jsonPath("$.[*].gols_v").value(hasItem(DEFAULT_GOLS_V)))
                .andExpect(jsonPath("$.[*].gols_l").value(hasItem(DEFAULT_GOLS_L)))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
                .andExpect(jsonPath("$.[*].arbitre").value(hasItem(DEFAULT_ARBITRE.toString())));
    }

    @Test
    @Transactional
    public void getPartit() throws Exception {
        // Initialize the database
        partitRepository.saveAndFlush(partit);

        // Get the partit
        restPartitMockMvc.perform(get("/api/partits/{id}", partit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(partit.getId().intValue()))
            .andExpect(jsonPath("$.nom_v").value(DEFAULT_NOM_V.toString()))
            .andExpect(jsonPath("$.nom_l").value(DEFAULT_NOM_L.toString()))
            .andExpect(jsonPath("$.gols_v").value(DEFAULT_GOLS_V))
            .andExpect(jsonPath("$.gols_l").value(DEFAULT_GOLS_L))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.arbitre").value(DEFAULT_ARBITRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPartit() throws Exception {
        // Get the partit
        restPartitMockMvc.perform(get("/api/partits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartit() throws Exception {
        // Initialize the database
        partitRepository.saveAndFlush(partit);

		int databaseSizeBeforeUpdate = partitRepository.findAll().size();

        // Update the partit
        partit.setNom_v(UPDATED_NOM_V);
        partit.setNom_l(UPDATED_NOM_L);
        partit.setGols_v(UPDATED_GOLS_V);
        partit.setGols_l(UPDATED_GOLS_L);
        partit.setData(UPDATED_DATA);
        partit.setArbitre(UPDATED_ARBITRE);
        restPartitMockMvc.perform(put("/api/partits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partit)))
                .andExpect(status().isOk());

        // Validate the Partit in the database
        List<Partit> partits = partitRepository.findAll();
        assertThat(partits).hasSize(databaseSizeBeforeUpdate);
        Partit testPartit = partits.get(partits.size() - 1);
        assertThat(testPartit.getNom_v()).isEqualTo(UPDATED_NOM_V);
        assertThat(testPartit.getNom_l()).isEqualTo(UPDATED_NOM_L);
        assertThat(testPartit.getGols_v()).isEqualTo(UPDATED_GOLS_V);
        assertThat(testPartit.getGols_l()).isEqualTo(UPDATED_GOLS_L);
        assertThat(testPartit.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testPartit.getArbitre()).isEqualTo(UPDATED_ARBITRE);
    }

    @Test
    @Transactional
    public void deletePartit() throws Exception {
        // Initialize the database
        partitRepository.saveAndFlush(partit);

		int databaseSizeBeforeDelete = partitRepository.findAll().size();

        // Get the partit
        restPartitMockMvc.perform(delete("/api/partits/{id}", partit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Partit> partits = partitRepository.findAll();
        assertThat(partits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
