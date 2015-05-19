package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Grup;
import net.yosef.repository.GrupRepository;
import net.yosef.repository.search.GrupSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GrupResource REST controller.
 *
 * @see GrupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GrupResourceTest {

    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";

    @Inject
    private GrupRepository grupRepository;

    @Inject
    private GrupSearchRepository grupSearchRepository;

    private MockMvc restGrupMockMvc;

    private Grup grup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GrupResource grupResource = new GrupResource();
        ReflectionTestUtils.setField(grupResource, "grupRepository", grupRepository);
        ReflectionTestUtils.setField(grupResource, "grupSearchRepository", grupSearchRepository);
        this.restGrupMockMvc = MockMvcBuilders.standaloneSetup(grupResource).build();
    }

    @Before
    public void initTest() {
        grup = new Grup();
        grup.setNom(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createGrup() throws Exception {
        int databaseSizeBeforeCreate = grupRepository.findAll().size();

        // Create the Grup
        restGrupMockMvc.perform(post("/api/grups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(grup)))
                .andExpect(status().isCreated());

        // Validate the Grup in the database
        List<Grup> grups = grupRepository.findAll();
        assertThat(grups).hasSize(databaseSizeBeforeCreate + 1);
        Grup testGrup = grups.get(grups.size() - 1);
        assertThat(testGrup.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(grupRepository.findAll()).hasSize(0);
        // set the field null
        grup.setNom(null);

        // Create the Grup, which fails.
        restGrupMockMvc.perform(post("/api/grups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(grup)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Grup> grups = grupRepository.findAll();
        assertThat(grups).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllGrups() throws Exception {
        // Initialize the database
        grupRepository.saveAndFlush(grup);

        // Get all the grups
        restGrupMockMvc.perform(get("/api/grups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(grup.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getGrup() throws Exception {
        // Initialize the database
        grupRepository.saveAndFlush(grup);

        // Get the grup
        restGrupMockMvc.perform(get("/api/grups/{id}", grup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(grup.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGrup() throws Exception {
        // Get the grup
        restGrupMockMvc.perform(get("/api/grups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGrup() throws Exception {
        // Initialize the database
        grupRepository.saveAndFlush(grup);

		int databaseSizeBeforeUpdate = grupRepository.findAll().size();

        // Update the grup
        grup.setNom(UPDATED_NOM);
        restGrupMockMvc.perform(put("/api/grups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(grup)))
                .andExpect(status().isOk());

        // Validate the Grup in the database
        List<Grup> grups = grupRepository.findAll();
        assertThat(grups).hasSize(databaseSizeBeforeUpdate);
        Grup testGrup = grups.get(grups.size() - 1);
        assertThat(testGrup.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void deleteGrup() throws Exception {
        // Initialize the database
        grupRepository.saveAndFlush(grup);

		int databaseSizeBeforeDelete = grupRepository.findAll().size();

        // Get the grup
        restGrupMockMvc.perform(delete("/api/grups/{id}", grup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Grup> grups = grupRepository.findAll();
        assertThat(grups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
