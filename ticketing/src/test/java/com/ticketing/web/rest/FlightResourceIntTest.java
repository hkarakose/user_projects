package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.Flight;
import com.ticketing.repository.FlightRepository;
import com.ticketing.repository.search.FlightSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ticketing.domain.enumeration.Status;
/**
 * Test class for the FlightResource REST controller.
 *
 * @see FlightResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class FlightResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_FLIGHT_CODE = "AAAAA";
    private static final String UPDATED_FLIGHT_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.ACTIVE;

    @Inject
    private FlightRepository flightRepository;

    @Inject
    private FlightSearchRepository flightSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFlightMockMvc;

    private Flight flight;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlightResource flightResource = new FlightResource();
        ReflectionTestUtils.setField(flightResource, "flightSearchRepository", flightSearchRepository);
        ReflectionTestUtils.setField(flightResource, "flightRepository", flightRepository);
        this.restFlightMockMvc = MockMvcBuilders.standaloneSetup(flightResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight();
        flight = new Flight()
                .flightCode(DEFAULT_FLIGHT_CODE)
                .date(DEFAULT_DATE)
                .status(DEFAULT_STATUS);
        return flight;
    }

    @Before
    public void initTest() {
        flightSearchRepository.deleteAll();
        flight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight

        restFlightMockMvc.perform(post("/api/flights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flight)))
                .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flights = flightRepository.findAll();
        assertThat(flights).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flights.get(flights.size() - 1);
        assertThat(testFlight.getFlightCode()).isEqualTo(DEFAULT_FLIGHT_CODE);
        assertThat(testFlight.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFlight.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Flight in ElasticSearch
        Flight flightEs = flightSearchRepository.findOne(testFlight.getId());
        assertThat(flightEs).isEqualToComparingFieldByField(testFlight);
    }

    @Test
    @Transactional
    public void checkFlightCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setFlightCode(null);

        // Create the Flight, which fails.

        restFlightMockMvc.perform(post("/api/flights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flight)))
                .andExpect(status().isBadRequest());

        List<Flight> flights = flightRepository.findAll();
        assertThat(flights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setDate(null);

        // Create the Flight, which fails.

        restFlightMockMvc.perform(post("/api/flights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flight)))
                .andExpect(status().isBadRequest());

        List<Flight> flights = flightRepository.findAll();
        assertThat(flights).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flights
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
                .andExpect(jsonPath("$.[*].flightCode").value(hasItem(DEFAULT_FLIGHT_CODE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.flightCode").value(DEFAULT_FLIGHT_CODE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        flightSearchRepository.save(flight);
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findOne(flight.getId());
        updatedFlight
                .flightCode(UPDATED_FLIGHT_CODE)
                .date(UPDATED_DATE)
                .status(UPDATED_STATUS);

        restFlightMockMvc.perform(put("/api/flights")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFlight)))
                .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flights = flightRepository.findAll();
        assertThat(flights).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flights.get(flights.size() - 1);
        assertThat(testFlight.getFlightCode()).isEqualTo(UPDATED_FLIGHT_CODE);
        assertThat(testFlight.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFlight.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Flight in ElasticSearch
        Flight flightEs = flightSearchRepository.findOne(testFlight.getId());
        assertThat(flightEs).isEqualToComparingFieldByField(testFlight);
    }

    @Test
    @Transactional
    public void deleteFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        flightSearchRepository.save(flight);
        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Get the flight
        restFlightMockMvc.perform(delete("/api/flights/{id}", flight.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean flightExistsInEs = flightSearchRepository.exists(flight.getId());
        assertThat(flightExistsInEs).isFalse();

        // Validate the database is empty
        List<Flight> flights = flightRepository.findAll();
        assertThat(flights).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);
        flightSearchRepository.save(flight);

        // Search the flight
        restFlightMockMvc.perform(get("/api/_search/flights?query=id:" + flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].flightCode").value(hasItem(DEFAULT_FLIGHT_CODE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
