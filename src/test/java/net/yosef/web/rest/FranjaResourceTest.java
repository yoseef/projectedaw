package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Franja;
import net.yosef.repository.FranjaRepository;
import net.yosef.repository.search.FranjaSearchRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FranjaResource REST controller.
 *
 * @see FranjaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FranjaResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final LocalDate DEFAULT_DIA = new LocalDate(0L);
    private static final LocalDate UPDATED_DIA = new LocalDate();

    private static final DateTime DEFAULT_HORA_INICI = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_HORA_INICI = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_HORA_INICI_STR = dateTimeFormatter.print(DEFAULT_HORA_INICI);

    private static final DateTime DEFAULT_HORA_FI = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_HORA_FI = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_HORA_FI_STR = dateTimeFormatter.print(DEFAULT_HORA_FI);

    @Inject
    private FranjaRepository franjaRepository;

    @Inject
    private FranjaSearchRepository franjaSearchRepository;

    private MockMvc restFranjaMockMvc;

    private Franja franja;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FranjaResource franjaResource = new FranjaResource();
        ReflectionTestUtils.setField(franjaResource, "franjaRepository", franjaRepository);
        ReflectionTestUtils.setField(franjaResource, "franjaSearchRepository", franjaSearchRepository);
        this.restFranjaMockMvc = MockMvcBuilders.standaloneSetup(franjaResource).build();
    }

    @Before
    public void initTest() {
        franja = new Franja();
        franja.setDia(DEFAULT_DIA);
        franja.setHora_inici(DEFAULT_HORA_INICI);
        franja.setHora_fi(DEFAULT_HORA_FI);
    }

    @Test
    @Transactional
    public void createFranja() throws Exception {
        int databaseSizeBeforeCreate = franjaRepository.findAll().size();

        // Create the Franja
        restFranjaMockMvc.perform(post("/api/franjas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(franja)))
                .andExpect(status().isCreated());

        // Validate the Franja in the database
        List<Franja> franjas = franjaRepository.findAll();
        assertThat(franjas).hasSize(databaseSizeBeforeCreate + 1);
        Franja testFranja = franjas.get(franjas.size() - 1);
        assertThat(testFranja.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testFranja.getHora_inici().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_HORA_INICI);
        assertThat(testFranja.getHora_fi().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_HORA_FI);
    }

    @Test
    @Transactional
    public void getAllFranjas() throws Exception {
        // Initialize the database
        franjaRepository.saveAndFlush(franja);

        // Get all the franjas
        restFranjaMockMvc.perform(get("/api/franjas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(franja.getId().intValue())))
                .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.toString())))
                .andExpect(jsonPath("$.[*].hora_inici").value(hasItem(DEFAULT_HORA_INICI_STR)))
                .andExpect(jsonPath("$.[*].hora_fi").value(hasItem(DEFAULT_HORA_FI_STR)));
    }

    @Test
    @Transactional
    public void getFranja() throws Exception {
        // Initialize the database
        franjaRepository.saveAndFlush(franja);

        // Get the franja
        restFranjaMockMvc.perform(get("/api/franjas/{id}", franja.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(franja.getId().intValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.toString()))
            .andExpect(jsonPath("$.hora_inici").value(DEFAULT_HORA_INICI_STR))
            .andExpect(jsonPath("$.hora_fi").value(DEFAULT_HORA_FI_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFranja() throws Exception {
        // Get the franja
        restFranjaMockMvc.perform(get("/api/franjas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFranja() throws Exception {
        // Initialize the database
        franjaRepository.saveAndFlush(franja);

		int databaseSizeBeforeUpdate = franjaRepository.findAll().size();

        // Update the franja
        franja.setDia(UPDATED_DIA);
        franja.setHora_inici(UPDATED_HORA_INICI);
        franja.setHora_fi(UPDATED_HORA_FI);
        restFranjaMockMvc.perform(put("/api/franjas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(franja)))
                .andExpect(status().isOk());

        // Validate the Franja in the database
        List<Franja> franjas = franjaRepository.findAll();
        assertThat(franjas).hasSize(databaseSizeBeforeUpdate);
        Franja testFranja = franjas.get(franjas.size() - 1);
        assertThat(testFranja.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFranja.getHora_inici().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_HORA_INICI);
        assertThat(testFranja.getHora_fi().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_HORA_FI);
    }

    @Test
    @Transactional
    public void deleteFranja() throws Exception {
        // Initialize the database
        franjaRepository.saveAndFlush(franja);

		int databaseSizeBeforeDelete = franjaRepository.findAll().size();

        // Get the franja
        restFranjaMockMvc.perform(delete("/api/franjas/{id}", franja.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Franja> franjas = franjaRepository.findAll();
        assertThat(franjas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
