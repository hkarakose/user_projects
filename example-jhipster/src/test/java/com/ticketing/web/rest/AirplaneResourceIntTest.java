package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.Airplane;
import com.ticketing.repository.AirplaneRepository;
import com.ticketing.repository.search.AirplaneSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AirplaneResource REST controller.
 *
 * @see AirplaneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class AirplaneResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AirplaneRepository airplaneRepository;

    @Inject
    private AirplaneSearchRepository airplaneSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAirplaneMockMvc;

    private Airplane airplane;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AirplaneResource airplaneResource = new AirplaneResource();
        ReflectionTestUtils.setField(airplaneResource, "airplaneSearchRepository", airplaneSearchRepository);
        ReflectionTestUtils.setField(airplaneResource, "airplaneRepository", airplaneRepository);
        this.restAirplaneMockMvc = MockMvcBuilders.standaloneSetup(airplaneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airplane createEntity(EntityManager em) {
        Airplane airplane = new Airplane();
        airplane = new Airplane()
                .name(DEFAULT_NAME);
        return airplane;
    }

    @Before
    public void initTest() {
        airplaneSearchRepository.deleteAll();
        airplane = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirplane() throws Exception {
        int databaseSizeBeforeCreate = airplaneRepository.findAll().size();

        // Create the Airplane

        restAirplaneMockMvc.perform(post("/api/airplanes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplane)))
                .andExpect(status().isCreated());

        // Validate the Airplane in the database
        List<Airplane> airplanes = airplaneRepository.findAll();
        assertThat(airplanes).hasSize(databaseSizeBeforeCreate + 1);
        Airplane testAirplane = airplanes.get(airplanes.size() - 1);
        assertThat(testAirplane.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Airplane in ElasticSearch
        Airplane airplaneEs = airplaneSearchRepository.findOne(testAirplane.getId());
        assertThat(airplaneEs).isEqualToComparingFieldByField(testAirplane);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airplaneRepository.findAll().size();
        // set the field null
        airplane.setName(null);

        // Create the Airplane, which fails.

        restAirplaneMockMvc.perform(post("/api/airplanes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplane)))
                .andExpect(status().isBadRequest());

        List<Airplane> airplanes = airplaneRepository.findAll();
        assertThat(airplanes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirplanes() throws Exception {
        // Initialize the database
        airplaneRepository.saveAndFlush(airplane);

        // Get all the airplanes
        restAirplaneMockMvc.perform(get("/api/airplanes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(airplane.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAirplane() throws Exception {
        // Initialize the database
        airplaneRepository.saveAndFlush(airplane);

        // Get the airplane
        restAirplaneMockMvc.perform(get("/api/airplanes/{id}", airplane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(airplane.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAirplane() throws Exception {
        // Get the airplane
        restAirplaneMockMvc.perform(get("/api/airplanes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirplane() throws Exception {
        // Initialize the database
        airplaneRepository.saveAndFlush(airplane);
        airplaneSearchRepository.save(airplane);
        int databaseSizeBeforeUpdate = airplaneRepository.findAll().size();

        // Update the airplane
        Airplane updatedAirplane = airplaneRepository.findOne(airplane.getId());
        updatedAirplane
                .name(UPDATED_NAME);

        restAirplaneMockMvc.perform(put("/api/airplanes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAirplane)))
                .andExpect(status().isOk());

        // Validate the Airplane in the database
        List<Airplane> airplanes = airplaneRepository.findAll();
        assertThat(airplanes).hasSize(databaseSizeBeforeUpdate);
        Airplane testAirplane = airplanes.get(airplanes.size() - 1);
        assertThat(testAirplane.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Airplane in ElasticSearch
        Airplane airplaneEs = airplaneSearchRepository.findOne(testAirplane.getId());
        assertThat(airplaneEs).isEqualToComparingFieldByField(testAirplane);
    }

    @Test
    @Transactional
    public void deleteAirplane() throws Exception {
        // Initialize the database
        airplaneRepository.saveAndFlush(airplane);
        airplaneSearchRepository.save(airplane);
        int databaseSizeBeforeDelete = airplaneRepository.findAll().size();

        // Get the airplane
        restAirplaneMockMvc.perform(delete("/api/airplanes/{id}", airplane.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean airplaneExistsInEs = airplaneSearchRepository.exists(airplane.getId());
        assertThat(airplaneExistsInEs).isFalse();

        // Validate the database is empty
        List<Airplane> airplanes = airplaneRepository.findAll();
        assertThat(airplanes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAirplane() throws Exception {
        // Initialize the database
        airplaneRepository.saveAndFlush(airplane);
        airplaneSearchRepository.save(airplane);

        // Search the airplane
        restAirplaneMockMvc.perform(get("/api/_search/airplanes?query=id:" + airplane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airplane.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
