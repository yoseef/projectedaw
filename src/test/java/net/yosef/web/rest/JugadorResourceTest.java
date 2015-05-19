package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Jugador;
import net.yosef.repository.JugadorRepository;
import net.yosef.repository.search.JugadorSearchRepository;

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
 * Test class for the JugadorResource REST controller.
 *
 * @see JugadorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JugadorResourceTest {

    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";

    private static final Integer DEFAULT_GOLS = 0;
    private static final Integer UPDATED_GOLS = 1;

    private static final LocalDate DEFAULT_ANY_NEIX = new LocalDate(0L);
    private static final LocalDate UPDATED_ANY_NEIX = new LocalDate();

    @Inject
    private JugadorRepository jugadorRepository;

    @Inject
    private JugadorSearchRepository jugadorSearchRepository;

    private MockMvc restJugadorMockMvc;

    private Jugador jugador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JugadorResource jugadorResource = new JugadorResource();
        ReflectionTestUtils.setField(jugadorResource, "jugadorRepository", jugadorRepository);
        ReflectionTestUtils.setField(jugadorResource, "jugadorSearchRepository", jugadorSearchRepository);
        this.restJugadorMockMvc = MockMvcBuilders.standaloneSetup(jugadorResource).build();
    }

    @Before
    public void initTest() {
        jugador = new Jugador();
        jugador.setNom(DEFAULT_NOM);
        jugador.setGols(DEFAULT_GOLS);
        jugador.setAny_neix(DEFAULT_ANY_NEIX);
    }

    @Test
    @Transactional
    public void createJugador() throws Exception {
        int databaseSizeBeforeCreate = jugadorRepository.findAll().size();

        // Create the Jugador
        restJugadorMockMvc.perform(post("/api/jugadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugador)))
                .andExpect(status().isCreated());

        // Validate the Jugador in the database
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeCreate + 1);
        Jugador testJugador = jugadors.get(jugadors.size() - 1);
        assertThat(testJugador.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testJugador.getGols()).isEqualTo(DEFAULT_GOLS);
        assertThat(testJugador.getAny_neix()).isEqualTo(DEFAULT_ANY_NEIX);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jugadorRepository.findAll()).hasSize(0);
        // set the field null
        jugador.setNom(null);

        // Create the Jugador, which fails.
        restJugadorMockMvc.perform(post("/api/jugadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugador)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllJugadors() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get all the jugadors
        restJugadorMockMvc.perform(get("/api/jugadors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jugador.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].gols").value(hasItem(DEFAULT_GOLS)))
                .andExpect(jsonPath("$.[*].any_neix").value(hasItem(DEFAULT_ANY_NEIX.toString())));
    }

    @Test
    @Transactional
    public void getJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", jugador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jugador.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.gols").value(DEFAULT_GOLS))
            .andExpect(jsonPath("$.any_neix").value(DEFAULT_ANY_NEIX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJugador() throws Exception {
        // Get the jugador
        restJugadorMockMvc.perform(get("/api/jugadors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

		int databaseSizeBeforeUpdate = jugadorRepository.findAll().size();

        // Update the jugador
        jugador.setNom(UPDATED_NOM);
        jugador.setGols(UPDATED_GOLS);
        jugador.setAny_neix(UPDATED_ANY_NEIX);
        restJugadorMockMvc.perform(put("/api/jugadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugador)))
                .andExpect(status().isOk());

        // Validate the Jugador in the database
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeUpdate);
        Jugador testJugador = jugadors.get(jugadors.size() - 1);
        assertThat(testJugador.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testJugador.getGols()).isEqualTo(UPDATED_GOLS);
        assertThat(testJugador.getAny_neix()).isEqualTo(UPDATED_ANY_NEIX);
    }

    @Test
    @Transactional
    public void deleteJugador() throws Exception {
        // Initialize the database
        jugadorRepository.saveAndFlush(jugador);

		int databaseSizeBeforeDelete = jugadorRepository.findAll().size();

        // Get the jugador
        restJugadorMockMvc.perform(delete("/api/jugadors/{id}", jugador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Jugador> jugadors = jugadorRepository.findAll();
        assertThat(jugadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
