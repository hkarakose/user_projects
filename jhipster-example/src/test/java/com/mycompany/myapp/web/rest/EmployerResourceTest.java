package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Employer;
import com.mycompany.myapp.repository.EmployerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployerResource REST controller.
 *
 * @see EmployerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployerResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_ESTABLISH_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ESTABLISH_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ESTABLISH_DATE_STR = dateTimeFormatter.print(DEFAULT_ESTABLISH_DATE);

    @Inject
    private EmployerRepository employerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployerMockMvc;

    private Employer employer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployerResource employerResource = new EmployerResource();
        ReflectionTestUtils.setField(employerResource, "employerRepository", employerRepository);
        this.restEmployerMockMvc = MockMvcBuilders.standaloneSetup(employerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employer = new Employer();
        employer.setTitle(DEFAULT_TITLE);
        employer.setEstablishDate(DEFAULT_ESTABLISH_DATE);
    }

    @Test
    @Transactional
    public void createEmployer() throws Exception {
        int databaseSizeBeforeCreate = employerRepository.findAll().size();

        // Create the Employer

        restEmployerMockMvc.perform(post("/api/employers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employer)))
                .andExpect(status().isCreated());

        // Validate the Employer in the database
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(databaseSizeBeforeCreate + 1);
        Employer testEmployer = employers.get(employers.size() - 1);
        assertThat(testEmployer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEmployer.getEstablishDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ESTABLISH_DATE);
    }

    @Test
    @Transactional
    public void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employers
        restEmployerMockMvc.perform(get("/api/employers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].establishDate").value(hasItem(DEFAULT_ESTABLISH_DATE_STR)));
    }

    @Test
    @Transactional
    public void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.establishDate").value(DEFAULT_ESTABLISH_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

		int databaseSizeBeforeUpdate = employerRepository.findAll().size();

        // Update the employer
        employer.setTitle(UPDATED_TITLE);
        employer.setEstablishDate(UPDATED_ESTABLISH_DATE);
        

        restEmployerMockMvc.perform(put("/api/employers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employer)))
                .andExpect(status().isOk());

        // Validate the Employer in the database
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(databaseSizeBeforeUpdate);
        Employer testEmployer = employers.get(employers.size() - 1);
        assertThat(testEmployer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEmployer.getEstablishDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ESTABLISH_DATE);
    }

    @Test
    @Transactional
    public void deleteEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

		int databaseSizeBeforeDelete = employerRepository.findAll().size();

        // Get the employer
        restEmployerMockMvc.perform(delete("/api/employers/{id}", employer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
