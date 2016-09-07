package com.ticketing.web.rest;

import com.ticketing.TicketingApp;
import com.ticketing.domain.AirplaneModel;
import com.ticketing.repository.AirplaneModelRepository;
import com.ticketing.repository.search.AirplaneModelSearchRepository;

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
 * Test class for the AirplaneModelResource REST controller.
 *
 * @see AirplaneModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingApp.class)
public class AirplaneModelResourceIntTest {
    private static final String DEFAULT_MODEL = "AAAAA";
    private static final String UPDATED_MODEL = "BBBBB";

    @Inject
    private AirplaneModelRepository airplaneModelRepository;

    @Inject
    private AirplaneModelSearchRepository airplaneModelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAirplaneModelMockMvc;

    private AirplaneModel airplaneModel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AirplaneModelResource airplaneModelResource = new AirplaneModelResource();
        ReflectionTestUtils.setField(airplaneModelResource, "airplaneModelSearchRepository", airplaneModelSearchRepository);
        ReflectionTestUtils.setField(airplaneModelResource, "airplaneModelRepository", airplaneModelRepository);
        this.restAirplaneModelMockMvc = MockMvcBuilders.standaloneSetup(airplaneModelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AirplaneModel createEntity(EntityManager em) {
        AirplaneModel airplaneModel = new AirplaneModel();
        airplaneModel = new AirplaneModel()
                .model(DEFAULT_MODEL);
        return airplaneModel;
    }

    @Before
    public void initTest() {
        airplaneModelSearchRepository.deleteAll();
        airplaneModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirplaneModel() throws Exception {
        int databaseSizeBeforeCreate = airplaneModelRepository.findAll().size();

        // Create the AirplaneModel

        restAirplaneModelMockMvc.perform(post("/api/airplane-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplaneModel)))
                .andExpect(status().isCreated());

        // Validate the AirplaneModel in the database
        List<AirplaneModel> airplaneModels = airplaneModelRepository.findAll();
        assertThat(airplaneModels).hasSize(databaseSizeBeforeCreate + 1);
        AirplaneModel testAirplaneModel = airplaneModels.get(airplaneModels.size() - 1);
        assertThat(testAirplaneModel.getModel()).isEqualTo(DEFAULT_MODEL);

        // Validate the AirplaneModel in ElasticSearch
        AirplaneModel airplaneModelEs = airplaneModelSearchRepository.findOne(testAirplaneModel.getId());
        assertThat(airplaneModelEs).isEqualToComparingFieldByField(testAirplaneModel);
    }

    @Test
    @Transactional
    public void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = airplaneModelRepository.findAll().size();
        // set the field null
        airplaneModel.setModel(null);

        // Create the AirplaneModel, which fails.

        restAirplaneModelMockMvc.perform(post("/api/airplane-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(airplaneModel)))
                .andExpect(status().isBadRequest());

        List<AirplaneModel> airplaneModels = airplaneModelRepository.findAll();
        assertThat(airplaneModels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirplaneModels() throws Exception {
        // Initialize the database
        airplaneModelRepository.saveAndFlush(airplaneModel);

        // Get all the airplaneModels
        restAirplaneModelMockMvc.perform(get("/api/airplane-models?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(airplaneModel.getId().intValue())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())));
    }

    @Test
    @Transactional
    public void getAirplaneModel() throws Exception {
        // Initialize the database
        airplaneModelRepository.saveAndFlush(airplaneModel);

        // Get the airplaneModel
        restAirplaneModelMockMvc.perform(get("/api/airplane-models/{id}", airplaneModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(airplaneModel.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAirplaneModel() throws Exception {
        // Get the airplaneModel
        restAirplaneModelMockMvc.perform(get("/api/airplane-models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirplaneModel() throws Exception {
        // Initialize the database
        airplaneModelRepository.saveAndFlush(airplaneModel);
        airplaneModelSearchRepository.save(airplaneModel);
        int databaseSizeBeforeUpdate = airplaneModelRepository.findAll().size();

        // Update the airplaneModel
        AirplaneModel updatedAirplaneModel = airplaneModelRepository.findOne(airplaneModel.getId());
        updatedAirplaneModel
                .model(UPDATED_MODEL);

        restAirplaneModelMockMvc.perform(put("/api/airplane-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAirplaneModel)))
                .andExpect(status().isOk());

        // Validate the AirplaneModel in the database
        List<AirplaneModel> airplaneModels = airplaneModelRepository.findAll();
        assertThat(airplaneModels).hasSize(databaseSizeBeforeUpdate);
        AirplaneModel testAirplaneModel = airplaneModels.get(airplaneModels.size() - 1);
        assertThat(testAirplaneModel.getModel()).isEqualTo(UPDATED_MODEL);

        // Validate the AirplaneModel in ElasticSearch
        AirplaneModel airplaneModelEs = airplaneModelSearchRepository.findOne(testAirplaneModel.getId());
        assertThat(airplaneModelEs).isEqualToComparingFieldByField(testAirplaneModel);
    }

    @Test
    @Transactional
    public void deleteAirplaneModel() throws Exception {
        // Initialize the database
        airplaneModelRepository.saveAndFlush(airplaneModel);
        airplaneModelSearchRepository.save(airplaneModel);
        int databaseSizeBeforeDelete = airplaneModelRepository.findAll().size();

        // Get the airplaneModel
        restAirplaneModelMockMvc.perform(delete("/api/airplane-models/{id}", airplaneModel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean airplaneModelExistsInEs = airplaneModelSearchRepository.exists(airplaneModel.getId());
        assertThat(airplaneModelExistsInEs).isFalse();

        // Validate the database is empty
        List<AirplaneModel> airplaneModels = airplaneModelRepository.findAll();
        assertThat(airplaneModels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAirplaneModel() throws Exception {
        // Initialize the database
        airplaneModelRepository.saveAndFlush(airplaneModel);
        airplaneModelSearchRepository.save(airplaneModel);

        // Search the airplaneModel
        restAirplaneModelMockMvc.perform(get("/api/_search/airplane-models?query=id:" + airplaneModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airplaneModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())));
    }
}
