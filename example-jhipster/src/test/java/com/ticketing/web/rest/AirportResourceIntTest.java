package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.Airport;
import com.ticketing.repository.AirportRepository;
import com.ticketing.repository.search.AirportSearchRepository;

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
 * Test class for the AirportResource REST controller.
 *
 * @see AirportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class AirportResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_IATA_CODE = "AAAAA";
    private static final String UPDATED_IATA_CODE = "BBBBB";

    @Inject
    private AirportRepository airportRepository;

    @Inject
    private AirportSearchRepository airportSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAirportMockMvc;

    private Airport airport;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AirportResource airportResource = new AirportResource();
        ReflectionTestUtils.setField(airportResource, "airportSearchRepository", airportSearchRepository);
        ReflectionTestUtils.setField(airportResource, "airportRepository", airportRepository);
        this.restAirportMockMvc = MockMvcBuilders.standaloneSetup(airportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airport createEntity(EntityManager em) {
        Airport airport = new Airport();
        airport = new Airport()
                .name(DEFAULT_NAME)
                .iataCode(DEFAULT_IATA_CODE);
        return airport;
    }

    @Before
    public void initTest() {
        airportSearchRepository.deleteAll();
        airport = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirport() throws Exception {
        int databaseSizeBeforeCreate = airportRepository.findAll().size();

        // Create the Airport

        restAirportMockMvc.perform(post("/api/airports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airport)))
                .andExpect(status().isCreated());

        // Validate the Airport in the database
        List<Airport> airports = airportRepository.findAll();
        assertThat(airports).hasSize(databaseSizeBeforeCreate + 1);
        Airport testAirport = airports.get(airports.size() - 1);
        assertThat(testAirport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAirport.getIataCode()).isEqualTo(DEFAULT_IATA_CODE);

        // Validate the Airport in ElasticSearch
        Airport airportEs = airportSearchRepository.findOne(testAirport.getId());
        assertThat(airportEs).isEqualToComparingFieldByField(testAirport);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setName(null);

        // Create the Airport, which fails.

        restAirportMockMvc.perform(post("/api/airports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airport)))
                .andExpect(status().isBadRequest());

        List<Airport> airports = airportRepository.findAll();
        assertThat(airports).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIataCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setIataCode(null);

        // Create the Airport, which fails.

        restAirportMockMvc.perform(post("/api/airports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airport)))
                .andExpect(status().isBadRequest());

        List<Airport> airports = airportRepository.findAll();
        assertThat(airports).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirports() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        // Get all the airports
        restAirportMockMvc.perform(get("/api/airports?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(airport.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].iataCode").value(hasItem(DEFAULT_IATA_CODE.toString())));
    }

    @Test
    @Transactional
    public void getAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        // Get the airport
        restAirportMockMvc.perform(get("/api/airports/{id}", airport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(airport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.iataCode").value(DEFAULT_IATA_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAirport() throws Exception {
        // Get the airport
        restAirportMockMvc.perform(get("/api/airports/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);
        airportSearchRepository.save(airport);
        int databaseSizeBeforeUpdate = airportRepository.findAll().size();

        // Update the airport
        Airport updatedAirport = airportRepository.findOne(airport.getId());
        updatedAirport
                .name(UPDATED_NAME)
                .iataCode(UPDATED_IATA_CODE);

        restAirportMockMvc.perform(put("/api/airports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAirport)))
                .andExpect(status().isOk());

        // Validate the Airport in the database
        List<Airport> airports = airportRepository.findAll();
        assertThat(airports).hasSize(databaseSizeBeforeUpdate);
        Airport testAirport = airports.get(airports.size() - 1);
        assertThat(testAirport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAirport.getIataCode()).isEqualTo(UPDATED_IATA_CODE);

        // Validate the Airport in ElasticSearch
        Airport airportEs = airportSearchRepository.findOne(testAirport.getId());
        assertThat(airportEs).isEqualToComparingFieldByField(testAirport);
    }

    @Test
    @Transactional
    public void deleteAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);
        airportSearchRepository.save(airport);
        int databaseSizeBeforeDelete = airportRepository.findAll().size();

        // Get the airport
        restAirportMockMvc.perform(delete("/api/airports/{id}", airport.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean airportExistsInEs = airportSearchRepository.exists(airport.getId());
        assertThat(airportExistsInEs).isFalse();

        // Validate the database is empty
        List<Airport> airports = airportRepository.findAll();
        assertThat(airports).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);
        airportSearchRepository.save(airport);

        // Search the airport
        restAirportMockMvc.perform(get("/api/_search/airports?query=id:" + airport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].iataCode").value(hasItem(DEFAULT_IATA_CODE.toString())));
    }
}
