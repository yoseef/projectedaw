package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Classificacio;
import net.yosef.repository.ClassificacioRepository;
import net.yosef.repository.search.ClassificacioSearchRepository;

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
 * Test class for the ClassificacioResource REST controller.
 *
 * @see ClassificacioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClassificacioResourceTest {


    private static final Integer DEFAULT_PUNTS = 0;
    private static final Integer UPDATED_PUNTS = 1;

    @Inject
    private ClassificacioRepository classificacioRepository;

    @Inject
    private ClassificacioSearchRepository classificacioSearchRepository;

    private MockMvc restClassificacioMockMvc;

    private Classificacio classificacio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassificacioResource classificacioResource = new ClassificacioResource();
        ReflectionTestUtils.setField(classificacioResource, "classificacioRepository", classificacioRepository);
        ReflectionTestUtils.setField(classificacioResource, "classificacioSearchRepository", classificacioSearchRepository);
        this.restClassificacioMockMvc = MockMvcBuilders.standaloneSetup(classificacioResource).build();
    }

    @Before
    public void initTest() {
        classificacio = new Classificacio();
        classificacio.setPunts(DEFAULT_PUNTS);
    }

    @Test
    @Transactional
    public void createClassificacio() throws Exception {
        int databaseSizeBeforeCreate = classificacioRepository.findAll().size();

        // Create the Classificacio
        restClassificacioMockMvc.perform(post("/api/classificacios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classificacio)))
                .andExpect(status().isCreated());

        // Validate the Classificacio in the database
        List<Classificacio> classificacios = classificacioRepository.findAll();
        assertThat(classificacios).hasSize(databaseSizeBeforeCreate + 1);
        Classificacio testClassificacio = classificacios.get(classificacios.size() - 1);
        assertThat(testClassificacio.getPunts()).isEqualTo(DEFAULT_PUNTS);
    }

    @Test
    @Transactional
    public void checkPuntsIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(classificacioRepository.findAll()).hasSize(0);
        // set the field null
        classificacio.setPunts(null);

        // Create the Classificacio, which fails.
        restClassificacioMockMvc.perform(post("/api/classificacios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classificacio)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Classificacio> classificacios = classificacioRepository.findAll();
        assertThat(classificacios).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllClassificacios() throws Exception {
        // Initialize the database
        classificacioRepository.saveAndFlush(classificacio);

        // Get all the classificacios
        restClassificacioMockMvc.perform(get("/api/classificacios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classificacio.getId().intValue())))
                .andExpect(jsonPath("$.[*].punts").value(hasItem(DEFAULT_PUNTS)));
    }

    @Test
    @Transactional
    public void getClassificacio() throws Exception {
        // Initialize the database
        classificacioRepository.saveAndFlush(classificacio);

        // Get the classificacio
        restClassificacioMockMvc.perform(get("/api/classificacios/{id}", classificacio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(classificacio.getId().intValue()))
            .andExpect(jsonPath("$.punts").value(DEFAULT_PUNTS));
    }

    @Test
    @Transactional
    public void getNonExistingClassificacio() throws Exception {
        // Get the classificacio
        restClassificacioMockMvc.perform(get("/api/classificacios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassificacio() throws Exception {
        // Initialize the database
        classificacioRepository.saveAndFlush(classificacio);

		int databaseSizeBeforeUpdate = classificacioRepository.findAll().size();

        // Update the classificacio
        classificacio.setPunts(UPDATED_PUNTS);
        restClassificacioMockMvc.perform(put("/api/classificacios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classificacio)))
                .andExpect(status().isOk());

        // Validate the Classificacio in the database
        List<Classificacio> classificacios = classificacioRepository.findAll();
        assertThat(classificacios).hasSize(databaseSizeBeforeUpdate);
        Classificacio testClassificacio = classificacios.get(classificacios.size() - 1);
        assertThat(testClassificacio.getPunts()).isEqualTo(UPDATED_PUNTS);
    }

    @Test
    @Transactional
    public void deleteClassificacio() throws Exception {
        // Initialize the database
        classificacioRepository.saveAndFlush(classificacio);

		int databaseSizeBeforeDelete = classificacioRepository.findAll().size();

        // Get the classificacio
        restClassificacioMockMvc.perform(delete("/api/classificacios/{id}", classificacio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Classificacio> classificacios = classificacioRepository.findAll();
        assertThat(classificacios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
