package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Jornada;
import net.yosef.repository.JornadaRepository;
import net.yosef.repository.search.JornadaSearchRepository;

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
 * Test class for the JornadaResource REST controller.
 *
 * @see JornadaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JornadaResourceTest {


    private static final Integer DEFAULT_NUMERO = 0;
    private static final Integer UPDATED_NUMERO = 1;

    @Inject
    private JornadaRepository jornadaRepository;

    @Inject
    private JornadaSearchRepository jornadaSearchRepository;

    private MockMvc restJornadaMockMvc;

    private Jornada jornada;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JornadaResource jornadaResource = new JornadaResource();
        ReflectionTestUtils.setField(jornadaResource, "jornadaRepository", jornadaRepository);
        ReflectionTestUtils.setField(jornadaResource, "jornadaSearchRepository", jornadaSearchRepository);
        this.restJornadaMockMvc = MockMvcBuilders.standaloneSetup(jornadaResource).build();
    }

    @Before
    public void initTest() {
        jornada = new Jornada();
        jornada.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createJornada() throws Exception {
        int databaseSizeBeforeCreate = jornadaRepository.findAll().size();

        // Create the Jornada
        restJornadaMockMvc.perform(post("/api/jornadas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jornada)))
                .andExpect(status().isCreated());

        // Validate the Jornada in the database
        List<Jornada> jornadas = jornadaRepository.findAll();
        assertThat(jornadas).hasSize(databaseSizeBeforeCreate + 1);
        Jornada testJornada = jornadas.get(jornadas.size() - 1);
        assertThat(testJornada.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jornadaRepository.findAll()).hasSize(0);
        // set the field null
        jornada.setNumero(null);

        // Create the Jornada, which fails.
        restJornadaMockMvc.perform(post("/api/jornadas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jornada)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Jornada> jornadas = jornadaRepository.findAll();
        assertThat(jornadas).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllJornadas() throws Exception {
        // Initialize the database
        jornadaRepository.saveAndFlush(jornada);

        // Get all the jornadas
        restJornadaMockMvc.perform(get("/api/jornadas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jornada.getId().intValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getJornada() throws Exception {
        // Initialize the database
        jornadaRepository.saveAndFlush(jornada);

        // Get the jornada
        restJornadaMockMvc.perform(get("/api/jornadas/{id}", jornada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jornada.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingJornada() throws Exception {
        // Get the jornada
        restJornadaMockMvc.perform(get("/api/jornadas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJornada() throws Exception {
        // Initialize the database
        jornadaRepository.saveAndFlush(jornada);

		int databaseSizeBeforeUpdate = jornadaRepository.findAll().size();

        // Update the jornada
        jornada.setNumero(UPDATED_NUMERO);
        restJornadaMockMvc.perform(put("/api/jornadas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jornada)))
                .andExpect(status().isOk());

        // Validate the Jornada in the database
        List<Jornada> jornadas = jornadaRepository.findAll();
        assertThat(jornadas).hasSize(databaseSizeBeforeUpdate);
        Jornada testJornada = jornadas.get(jornadas.size() - 1);
        assertThat(testJornada.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteJornada() throws Exception {
        // Initialize the database
        jornadaRepository.saveAndFlush(jornada);

		int databaseSizeBeforeDelete = jornadaRepository.findAll().size();

        // Get the jornada
        restJornadaMockMvc.perform(delete("/api/jornadas/{id}", jornada.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Jornada> jornadas = jornadaRepository.findAll();
        assertThat(jornadas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
