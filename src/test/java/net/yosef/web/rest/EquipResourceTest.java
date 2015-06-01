package net.yosef.web.rest;

import net.yosef.Application;
import net.yosef.domain.Equip;
import net.yosef.domain.Grup;
import net.yosef.repository.EquipRepository;
import net.yosef.repository.GrupRepository;
import net.yosef.repository.search.EquipSearchRepository;

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
 * Test class for the EquipResource REST controller.
 *
 * @see EquipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EquipResourceTest {

    private static final String DEFAULT_NOM = "SAMPLE_TEXT";
    private static final String UPDATED_NOM = "UPDATED_TEXT";

    private static final Integer DEFAULT_GOLS_FAVOR = 0;
    private static final Integer UPDATED_GOLS_FAVOR = 1;

    private static final Integer DEFAULT_GOLS_CONTRA = 0;
    private static final Integer UPDATED_GOLS_CONTRA = 1;

    private static final Integer DEFAULT_PJ = 0;
    private static final Integer UPDATED_PJ = 1;

    private static final Integer DEFAULT_PG = 0;
    private static final Integer UPDATED_PG = 1;

    private static final Integer DEFAULT_PE = 0;
    private static final Integer UPDATED_PE = 1;

    private static final Integer DEFAULT_PP = 0;
    private static final Integer UPDATED_PP = 1;

    private static final LocalDate DEFAULT_DATA_ALTA = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA_ALTA = new LocalDate();

    private static final Boolean DEFAULT_PAGAT = false;
    private static final Boolean UPDATED_PAGAT = true;

    @Inject
    private EquipRepository equipRepository;

    @Inject
    private EquipSearchRepository equipSearchRepository;
    @Inject
    private GrupRepository grupRepo;

    private MockMvc restEquipMockMvc;

    private Equip equip;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipResource equipResource = new EquipResource();
        ReflectionTestUtils.setField(equipResource, "equipRepository", equipRepository);
        ReflectionTestUtils.setField(equipResource, "equipSearchRepository", equipSearchRepository);
        this.restEquipMockMvc = MockMvcBuilders.standaloneSetup(equipResource).build();
    }

    @Before
    public void initTest() {
        Grup g = new Grup("prova");
        grupRepo.save(g);

        equip = new Equip();
        equip.setNom(DEFAULT_NOM);
        equip.setGols_favor(DEFAULT_GOLS_FAVOR);
        equip.setGols_contra(DEFAULT_GOLS_CONTRA);
        equip.setPj(DEFAULT_PJ);
        equip.setPg(DEFAULT_PG);
        equip.setPe(DEFAULT_PE);
        equip.setPp(DEFAULT_PP);
        equip.setData_alta(DEFAULT_DATA_ALTA);
        equip.setPagat(DEFAULT_PAGAT);
        equip.setGrup(g);
    }

    @Test
    @Transactional
    public void createEquip() throws Exception {
        int databaseSizeBeforeCreate = equipRepository.findAll().size();

        // Create the Equip
        restEquipMockMvc.perform(post("/api/equips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equip)))
            .andExpect(status().isCreated());

        // Validate the Equip in the database
        List<Equip> equips = equipRepository.findAll();
        assertThat(equips).hasSize(databaseSizeBeforeCreate + 1);
        Equip testEquip = equips.get(equips.size() - 1);
        assertThat(testEquip.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEquip.getGols_favor()).isEqualTo(DEFAULT_GOLS_FAVOR);
        assertThat(testEquip.getGols_contra()).isEqualTo(DEFAULT_GOLS_CONTRA);
        assertThat(testEquip.getPj()).isEqualTo(DEFAULT_PJ);
        assertThat(testEquip.getPg()).isEqualTo(DEFAULT_PG);
        assertThat(testEquip.getPe()).isEqualTo(DEFAULT_PE);
        assertThat(testEquip.getPp()).isEqualTo(DEFAULT_PP);
        assertThat(testEquip.getData_alta()).isEqualTo(DEFAULT_DATA_ALTA);
        assertThat(testEquip.getPagat()).isEqualTo(DEFAULT_PAGAT);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(equipRepository.findAll()).hasSize(0);
        // set the field null
        equip.setNom(null);

        // Create the Equip, which fails.
        restEquipMockMvc.perform(post("/api/equips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equip)))
            .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Equip> equips = equipRepository.findAll();
        assertThat(equips).hasSize(0);
    }

    @Test
    @Transactional
    public void checkData_altaIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(equipRepository.findAll()).hasSize(0);
        // set the field null
        equip.setData_alta(null);

        // Create the Equip, which fails.
        restEquipMockMvc.perform(post("/api/equips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equip)))
            .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Equip> equips = equipRepository.findAll();
        assertThat(equips).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllEquips() throws Exception {
        // Initialize the database
        equipRepository.saveAndFlush(equip);

        // Get all the equips
        restEquipMockMvc.perform(get("/api/equips"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equip.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].gols_favor").value(hasItem(DEFAULT_GOLS_FAVOR)))
            .andExpect(jsonPath("$.[*].gols_contra").value(hasItem(DEFAULT_GOLS_CONTRA)))
            .andExpect(jsonPath("$.[*].pj").value(hasItem(DEFAULT_PJ)))
            .andExpect(jsonPath("$.[*].pg").value(hasItem(DEFAULT_PG)))
            .andExpect(jsonPath("$.[*].pe").value(hasItem(DEFAULT_PE)))
            .andExpect(jsonPath("$.[*].pp").value(hasItem(DEFAULT_PP)))
            .andExpect(jsonPath("$.[*].data_alta").value(hasItem(DEFAULT_DATA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].pagat").value(hasItem(DEFAULT_PAGAT.booleanValue())));
    }

    @Test
    @Transactional
    public void getEquip() throws Exception {
        // Initialize the database
        equipRepository.saveAndFlush(equip);

        // Get the equip
        restEquipMockMvc.perform(get("/api/equips/{id}", equip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(equip.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.gols_favor").value(DEFAULT_GOLS_FAVOR))
            .andExpect(jsonPath("$.gols_contra").value(DEFAULT_GOLS_CONTRA))
            .andExpect(jsonPath("$.pj").value(DEFAULT_PJ))
            .andExpect(jsonPath("$.pg").value(DEFAULT_PG))
            .andExpect(jsonPath("$.pe").value(DEFAULT_PE))
            .andExpect(jsonPath("$.pp").value(DEFAULT_PP))
            .andExpect(jsonPath("$.data_alta").value(DEFAULT_DATA_ALTA.toString()))
            .andExpect(jsonPath("$.pagat").value(DEFAULT_PAGAT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEquip() throws Exception {
        // Get the equip
        restEquipMockMvc.perform(get("/api/equips/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquip() throws Exception {
        // Initialize the database
        equipRepository.saveAndFlush(equip);

        int databaseSizeBeforeUpdate = equipRepository.findAll().size();

        // Update the equip
        equip.setNom(UPDATED_NOM);
        equip.setGols_favor(UPDATED_GOLS_FAVOR);
        equip.setGols_contra(UPDATED_GOLS_CONTRA);
        equip.setPj(UPDATED_PJ);
        equip.setPg(UPDATED_PG);
        equip.setPe(UPDATED_PE);
        equip.setPp(UPDATED_PP);
        equip.setData_alta(UPDATED_DATA_ALTA);
        equip.setPagat(UPDATED_PAGAT);
        restEquipMockMvc.perform(put("/api/equips")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equip)))
            .andExpect(status().isOk());

        // Validate the Equip in the database
        List<Equip> equips = equipRepository.findAll();
        assertThat(equips).hasSize(databaseSizeBeforeUpdate);
        Equip testEquip = equips.get(equips.size() - 1);
        assertThat(testEquip.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEquip.getGols_favor()).isEqualTo(UPDATED_GOLS_FAVOR);
        assertThat(testEquip.getGols_contra()).isEqualTo(UPDATED_GOLS_CONTRA);
        assertThat(testEquip.getPj()).isEqualTo(UPDATED_PJ);
        assertThat(testEquip.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testEquip.getPe()).isEqualTo(UPDATED_PE);
        assertThat(testEquip.getPp()).isEqualTo(UPDATED_PP);
        assertThat(testEquip.getData_alta()).isEqualTo(UPDATED_DATA_ALTA);
        assertThat(testEquip.getPagat()).isEqualTo(UPDATED_PAGAT);
    }

    @Test
    @Transactional
    public void deleteEquip() throws Exception {
        // Initialize the database
        equipRepository.saveAndFlush(equip);

        int databaseSizeBeforeDelete = equipRepository.findAll().size();

        // Get the equip
        restEquipMockMvc.perform(delete("/api/equips/{id}", equip.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Equip> equips = equipRepository.findAll();
        assertThat(equips).hasSize(databaseSizeBeforeDelete - 1);
    }
}
