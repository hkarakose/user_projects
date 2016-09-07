package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.FlightSeat;
import com.ticketing.repository.FlightSeatRepository;
import com.ticketing.repository.search.FlightSeatSearchRepository;

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
 * Test class for the FlightSeatResource REST controller.
 *
 * @see FlightSeatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class FlightSeatResourceIntTest {
    private static final String DEFAULT_SEAT_NO = "AAAAA";
    private static final String UPDATED_SEAT_NO = "BBBBB";

    private static final Boolean DEFAULT_AVAILABILITY = false;
    private static final Boolean UPDATED_AVAILABILITY = true;

    @Inject
    private FlightSeatRepository flightSeatRepository;

    @Inject
    private FlightSeatSearchRepository flightSeatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFlightSeatMockMvc;

    private FlightSeat flightSeat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlightSeatResource flightSeatResource = new FlightSeatResource();
        ReflectionTestUtils.setField(flightSeatResource, "flightSeatSearchRepository", flightSeatSearchRepository);
        ReflectionTestUtils.setField(flightSeatResource, "flightSeatRepository", flightSeatRepository);
        this.restFlightSeatMockMvc = MockMvcBuilders.standaloneSetup(flightSeatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlightSeat createEntity(EntityManager em) {
        FlightSeat flightSeat = new FlightSeat();
        flightSeat = new FlightSeat()
                .seatNo(DEFAULT_SEAT_NO)
                .availability(DEFAULT_AVAILABILITY);
        return flightSeat;
    }

    @Before
    public void initTest() {
        flightSeatSearchRepository.deleteAll();
        flightSeat = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlightSeat() throws Exception {
        int databaseSizeBeforeCreate = flightSeatRepository.findAll().size();

        // Create the FlightSeat

        restFlightSeatMockMvc.perform(post("/api/flight-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSeat)))
                .andExpect(status().isCreated());

        // Validate the FlightSeat in the database
        List<FlightSeat> flightSeats = flightSeatRepository.findAll();
        assertThat(flightSeats).hasSize(databaseSizeBeforeCreate + 1);
        FlightSeat testFlightSeat = flightSeats.get(flightSeats.size() - 1);
        assertThat(testFlightSeat.getSeatNo()).isEqualTo(DEFAULT_SEAT_NO);
        assertThat(testFlightSeat.isAvailability()).isEqualTo(DEFAULT_AVAILABILITY);

        // Validate the FlightSeat in ElasticSearch
        FlightSeat flightSeatEs = flightSeatSearchRepository.findOne(testFlightSeat.getId());
        assertThat(flightSeatEs).isEqualToComparingFieldByField(testFlightSeat);
    }

    @Test
    @Transactional
    public void checkSeatNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightSeatRepository.findAll().size();
        // set the field null
        flightSeat.setSeatNo(null);

        // Create the FlightSeat, which fails.

        restFlightSeatMockMvc.perform(post("/api/flight-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightSeat)))
                .andExpect(status().isBadRequest());

        List<FlightSeat> flightSeats = flightSeatRepository.findAll();
        assertThat(flightSeats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlightSeats() throws Exception {
        // Initialize the database
        flightSeatRepository.saveAndFlush(flightSeat);

        // Get all the flightSeats
        restFlightSeatMockMvc.perform(get("/api/flight-seats?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flightSeat.getId().intValue())))
                .andExpect(jsonPath("$.[*].seatNo").value(hasItem(DEFAULT_SEAT_NO.toString())))
                .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.booleanValue())));
    }

    @Test
    @Transactional
    public void getFlightSeat() throws Exception {
        // Initialize the database
        flightSeatRepository.saveAndFlush(flightSeat);

        // Get the flightSeat
        restFlightSeatMockMvc.perform(get("/api/flight-seats/{id}", flightSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(flightSeat.getId().intValue()))
            .andExpect(jsonPath("$.seatNo").value(DEFAULT_SEAT_NO.toString()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFlightSeat() throws Exception {
        // Get the flightSeat
        restFlightSeatMockMvc.perform(get("/api/flight-seats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlightSeat() throws Exception {
        // Initialize the database
        flightSeatRepository.saveAndFlush(flightSeat);
        flightSeatSearchRepository.save(flightSeat);
        int databaseSizeBeforeUpdate = flightSeatRepository.findAll().size();

        // Update the flightSeat
        FlightSeat updatedFlightSeat = flightSeatRepository.findOne(flightSeat.getId());
        updatedFlightSeat
                .seatNo(UPDATED_SEAT_NO)
                .availability(UPDATED_AVAILABILITY);

        restFlightSeatMockMvc.perform(put("/api/flight-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFlightSeat)))
                .andExpect(status().isOk());

        // Validate the FlightSeat in the database
        List<FlightSeat> flightSeats = flightSeatRepository.findAll();
        assertThat(flightSeats).hasSize(databaseSizeBeforeUpdate);
        FlightSeat testFlightSeat = flightSeats.get(flightSeats.size() - 1);
        assertThat(testFlightSeat.getSeatNo()).isEqualTo(UPDATED_SEAT_NO);
        assertThat(testFlightSeat.isAvailability()).isEqualTo(UPDATED_AVAILABILITY);

        // Validate the FlightSeat in ElasticSearch
        FlightSeat flightSeatEs = flightSeatSearchRepository.findOne(testFlightSeat.getId());
        assertThat(flightSeatEs).isEqualToComparingFieldByField(testFlightSeat);
    }

    @Test
    @Transactional
    public void deleteFlightSeat() throws Exception {
        // Initialize the database
        flightSeatRepository.saveAndFlush(flightSeat);
        flightSeatSearchRepository.save(flightSeat);
        int databaseSizeBeforeDelete = flightSeatRepository.findAll().size();

        // Get the flightSeat
        restFlightSeatMockMvc.perform(delete("/api/flight-seats/{id}", flightSeat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean flightSeatExistsInEs = flightSeatSearchRepository.exists(flightSeat.getId());
        assertThat(flightSeatExistsInEs).isFalse();

        // Validate the database is empty
        List<FlightSeat> flightSeats = flightSeatRepository.findAll();
        assertThat(flightSeats).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFlightSeat() throws Exception {
        // Initialize the database
        flightSeatRepository.saveAndFlush(flightSeat);
        flightSeatSearchRepository.save(flightSeat);

        // Search the flightSeat
        restFlightSeatMockMvc.perform(get("/api/_search/flight-seats?query=id:" + flightSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flightSeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].seatNo").value(hasItem(DEFAULT_SEAT_NO.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.booleanValue())));
    }
}
