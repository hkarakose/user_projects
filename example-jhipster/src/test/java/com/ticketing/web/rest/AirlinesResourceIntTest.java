package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.Airlines;
import com.ticketing.repository.AirlinesRepository;
import com.ticketing.repository.search.AirlinesSearchRepository;

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
 * Test class for the AirlinesResource REST controller.
 *
 * @see AirlinesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class AirlinesResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ABBREVIATION = "AAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBB";

    @Inject
    private AirlinesRepository airlinesRepository;

    @Inject
    private AirlinesSearchRepository airlinesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAirlinesMockMvc;

    private Airlines airlines;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AirlinesResource airlinesResource = new AirlinesResource();
        ReflectionTestUtils.setField(airlinesResource, "airlinesSearchRepository", airlinesSearchRepository);
        ReflectionTestUtils.setField(airlinesResource, "airlinesRepository", airlinesRepository);
        this.restAirlinesMockMvc = MockMvcBuilders.standaloneSetup(airlinesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airlines createEntity(EntityManager em) {
        Airlines airlines = new Airlines();
        airlines = new Airlines()
                .name(DEFAULT_NAME)
                .abbreviation(DEFAULT_ABBREVIATION);
        return airlines;
    }

    @Before
    public void initTest() {
        airlinesSearchRepository.deleteAll();
        airlines = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirlines() throws Exception {
        int databaseSizeBeforeCreate = airlinesRepository.findAll().size();

        // Create the Airlines

        restAirlinesMockMvc.perform(post("/api/airlines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airlines)))
                .andExpect(status().isCreated());

        // Validate the Airlines in the database
        List<Airlines> airlines = airlinesRepository.findAll();
        assertThat(airlines).hasSize(databaseSizeBeforeCreate + 1);
        Airlines testAirlines = airlines.get(airlines.size() - 1);
        assertThat(testAirlines.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAirlines.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);

        // Validate the Airlines in ElasticSearch
        Airlines airlinesEs = airlinesSearchRepository.findOne(testAirlines.getId());
        assertThat(airlinesEs).isEqualToComparingFieldByField(testAirlines);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airlinesRepository.findAll().size();
        // set the field null
        airlines.setName(null);

        // Create the Airlines, which fails.

        restAirlinesMockMvc.perform(post("/api/airlines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airlines)))
                .andExpect(status().isBadRequest());

        List<Airlines> airlines = airlinesRepository.findAll();
        assertThat(airlines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirlines() throws Exception {
        // Initialize the database
        airlinesRepository.saveAndFlush(airlines);

        // Get all the airlines
        restAirlinesMockMvc.perform(get("/api/airlines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(airlines.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())));
    }

    @Test
    @Transactional
    public void getAirlines() throws Exception {
        // Initialize the database
        airlinesRepository.saveAndFlush(airlines);

        // Get the airlines
        restAirlinesMockMvc.perform(get("/api/airlines/{id}", airlines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(airlines.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAirlines() throws Exception {
        // Get the airlines
        restAirlinesMockMvc.perform(get("/api/airlines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirlines() throws Exception {
        // Initialize the database
        airlinesRepository.saveAndFlush(airlines);
        airlinesSearchRepository.save(airlines);
        int databaseSizeBeforeUpdate = airlinesRepository.findAll().size();

        // Update the airlines
        Airlines updatedAirlines = airlinesRepository.findOne(airlines.getId());
        updatedAirlines
                .name(UPDATED_NAME)
                .abbreviation(UPDATED_ABBREVIATION);

        restAirlinesMockMvc.perform(put("/api/airlines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAirlines)))
                .andExpect(status().isOk());

        // Validate the Airlines in the database
        List<Airlines> airlines = airlinesRepository.findAll();
        assertThat(airlines).hasSize(databaseSizeBeforeUpdate);
        Airlines testAirlines = airlines.get(airlines.size() - 1);
        assertThat(testAirlines.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAirlines.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);

        // Validate the Airlines in ElasticSearch
        Airlines airlinesEs = airlinesSearchRepository.findOne(testAirlines.getId());
        assertThat(airlinesEs).isEqualToComparingFieldByField(testAirlines);
    }

    @Test
    @Transactional
    public void deleteAirlines() throws Exception {
        // Initialize the database
        airlinesRepository.saveAndFlush(airlines);
        airlinesSearchRepository.save(airlines);
        int databaseSizeBeforeDelete = airlinesRepository.findAll().size();

        // Get the airlines
        restAirlinesMockMvc.perform(delete("/api/airlines/{id}", airlines.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean airlinesExistsInEs = airlinesSearchRepository.exists(airlines.getId());
        assertThat(airlinesExistsInEs).isFalse();

        // Validate the database is empty
        List<Airlines> airlines = airlinesRepository.findAll();
        assertThat(airlines).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAirlines() throws Exception {
        // Initialize the database
        airlinesRepository.saveAndFlush(airlines);
        airlinesSearchRepository.save(airlines);

        // Search the airlines
        restAirlinesMockMvc.perform(get("/api/_search/airlines?query=id:" + airlines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airlines.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())));
    }
}
