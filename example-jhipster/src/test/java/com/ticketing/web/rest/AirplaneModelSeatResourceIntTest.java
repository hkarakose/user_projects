package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.AirplaneModelSeat;
import com.ticketing.repository.AirplaneModelSeatRepository;
import com.ticketing.repository.search.AirplaneModelSeatSearchRepository;

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
 * Test class for the AirplaneModelSeatResource REST controller.
 *
 * @see AirplaneModelSeatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class AirplaneModelSeatResourceIntTest {

    private static final Long DEFAULT_MODEL_ID = 1L;
    private static final Long UPDATED_MODEL_ID = 2L;
    private static final String DEFAULT_SEAT_NO = "AAAAA";
    private static final String UPDATED_SEAT_NO = "BBBBB";

    @Inject
    private AirplaneModelSeatRepository airplaneModelSeatRepository;

    @Inject
    private AirplaneModelSeatSearchRepository airplaneModelSeatSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAirplaneModelSeatMockMvc;

    private AirplaneModelSeat airplaneModelSeat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AirplaneModelSeatResource airplaneModelSeatResource = new AirplaneModelSeatResource();
        ReflectionTestUtils.setField(airplaneModelSeatResource, "airplaneModelSeatSearchRepository", airplaneModelSeatSearchRepository);
        ReflectionTestUtils.setField(airplaneModelSeatResource, "airplaneModelSeatRepository", airplaneModelSeatRepository);
        this.restAirplaneModelSeatMockMvc = MockMvcBuilders.standaloneSetup(airplaneModelSeatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AirplaneModelSeat createEntity(EntityManager em) {
        AirplaneModelSeat airplaneModelSeat = new AirplaneModelSeat();
        airplaneModelSeat = new AirplaneModelSeat()
                .modelId(DEFAULT_MODEL_ID)
                .seatNo(DEFAULT_SEAT_NO);
        return airplaneModelSeat;
    }

    @Before
    public void initTest() {
        airplaneModelSeatSearchRepository.deleteAll();
        airplaneModelSeat = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirplaneModelSeat() throws Exception {
        int databaseSizeBeforeCreate = airplaneModelSeatRepository.findAll().size();

        // Create the AirplaneModelSeat

        restAirplaneModelSeatMockMvc.perform(post("/api/airplane-model-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplaneModelSeat)))
                .andExpect(status().isCreated());

        // Validate the AirplaneModelSeat in the database
        List<AirplaneModelSeat> airplaneModelSeats = airplaneModelSeatRepository.findAll();
        assertThat(airplaneModelSeats).hasSize(databaseSizeBeforeCreate + 1);
        AirplaneModelSeat testAirplaneModelSeat = airplaneModelSeats.get(airplaneModelSeats.size() - 1);
        assertThat(testAirplaneModelSeat.getModelId()).isEqualTo(DEFAULT_MODEL_ID);
        assertThat(testAirplaneModelSeat.getSeatNo()).isEqualTo(DEFAULT_SEAT_NO);

        // Validate the AirplaneModelSeat in ElasticSearch
        AirplaneModelSeat airplaneModelSeatEs = airplaneModelSeatSearchRepository.findOne(testAirplaneModelSeat.getId());
        assertThat(airplaneModelSeatEs).isEqualToComparingFieldByField(testAirplaneModelSeat);
    }

    @Test
    @Transactional
    public void checkSeatNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = airplaneModelSeatRepository.findAll().size();
        // set the field null
        airplaneModelSeat.setSeatNo(null);

        // Create the AirplaneModelSeat, which fails.

        restAirplaneModelSeatMockMvc.perform(post("/api/airplane-model-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplaneModelSeat)))
                .andExpect(status().isBadRequest());

        List<AirplaneModelSeat> airplaneModelSeats = airplaneModelSeatRepository.findAll();
        assertThat(airplaneModelSeats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirplaneModelSeats() throws Exception {
        // Initialize the database
        airplaneModelSeatRepository.saveAndFlush(airplaneModelSeat);

        // Get all the airplaneModelSeats
        restAirplaneModelSeatMockMvc.perform(get("/api/airplane-model-seats?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(airplaneModelSeat.getId().intValue())))
                .andExpect(jsonPath("$.[*].modelId").value(hasItem(DEFAULT_MODEL_ID.intValue())))
                .andExpect(jsonPath("$.[*].seatNo").value(hasItem(DEFAULT_SEAT_NO.toString())));
    }

    @Test
    @Transactional
    public void getAirplaneModelSeat() throws Exception {
        // Initialize the database
        airplaneModelSeatRepository.saveAndFlush(airplaneModelSeat);

        // Get the airplaneModelSeat
        restAirplaneModelSeatMockMvc.perform(get("/api/airplane-model-seats/{id}", airplaneModelSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(airplaneModelSeat.getId().intValue()))
            .andExpect(jsonPath("$.modelId").value(DEFAULT_MODEL_ID.intValue()))
            .andExpect(jsonPath("$.seatNo").value(DEFAULT_SEAT_NO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAirplaneModelSeat() throws Exception {
        // Get the airplaneModelSeat
        restAirplaneModelSeatMockMvc.perform(get("/api/airplane-model-seats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirplaneModelSeat() throws Exception {
        // Initialize the database
        airplaneModelSeatRepository.saveAndFlush(airplaneModelSeat);
        airplaneModelSeatSearchRepository.save(airplaneModelSeat);
        int databaseSizeBeforeUpdate = airplaneModelSeatRepository.findAll().size();

        // Update the airplaneModelSeat
        AirplaneModelSeat updatedAirplaneModelSeat = airplaneModelSeatRepository.findOne(airplaneModelSeat.getId());
        updatedAirplaneModelSeat
                .modelId(UPDATED_MODEL_ID)
                .seatNo(UPDATED_SEAT_NO);

        restAirplaneModelSeatMockMvc.perform(put("/api/airplane-model-seats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAirplaneModelSeat)))
                .andExpect(status().isOk());

        // Validate the AirplaneModelSeat in the database
        List<AirplaneModelSeat> airplaneModelSeats = airplaneModelSeatRepository.findAll();
        assertThat(airplaneModelSeats).hasSize(databaseSizeBeforeUpdate);
        AirplaneModelSeat testAirplaneModelSeat = airplaneModelSeats.get(airplaneModelSeats.size() - 1);
        assertThat(testAirplaneModelSeat.getModelId()).isEqualTo(UPDATED_MODEL_ID);
        assertThat(testAirplaneModelSeat.getSeatNo()).isEqualTo(UPDATED_SEAT_NO);

        // Validate the AirplaneModelSeat in ElasticSearch
        AirplaneModelSeat airplaneModelSeatEs = airplaneModelSeatSearchRepository.findOne(testAirplaneModelSeat.getId());
        assertThat(airplaneModelSeatEs).isEqualToComparingFieldByField(testAirplaneModelSeat);
    }

    @Test
    @Transactional
    public void deleteAirplaneModelSeat() throws Exception {
        // Initialize the database
        airplaneModelSeatRepository.saveAndFlush(airplaneModelSeat);
        airplaneModelSeatSearchRepository.save(airplaneModelSeat);
        int databaseSizeBeforeDelete = airplaneModelSeatRepository.findAll().size();

        // Get the airplaneModelSeat
        restAirplaneModelSeatMockMvc.perform(delete("/api/airplane-model-seats/{id}", airplaneModelSeat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean airplaneModelSeatExistsInEs = airplaneModelSeatSearchRepository.exists(airplaneModelSeat.getId());
        assertThat(airplaneModelSeatExistsInEs).isFalse();

        // Validate the database is empty
        List<AirplaneModelSeat> airplaneModelSeats = airplaneModelSeatRepository.findAll();
        assertThat(airplaneModelSeats).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAirplaneModelSeat() throws Exception {
        // Initialize the database
        airplaneModelSeatRepository.saveAndFlush(airplaneModelSeat);
        airplaneModelSeatSearchRepository.save(airplaneModelSeat);

        // Search the airplaneModelSeat
        restAirplaneModelSeatMockMvc.perform(get("/api/_search/airplane-model-seats?query=id:" + airplaneModelSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airplaneModelSeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelId").value(hasItem(DEFAULT_MODEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].seatNo").value(hasItem(DEFAULT_SEAT_NO.toString())));
    }
}
