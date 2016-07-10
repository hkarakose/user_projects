package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Employer;
import com.mycompany.myapp.repository.EmployerRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Employer.
 */
@RestController
@RequestMapping("/api")
public class EmployerResource {

    private final Logger log = LoggerFactory.getLogger(EmployerResource.class);

    @Inject
    private EmployerRepository employerRepository;

    /**
     * POST  /employers -> Create a new employer.
     */
    @RequestMapping(value = "/employers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employer> createEmployer(@RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to save Employer : {}", employer);
        if (employer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new employer cannot already have an ID").body(null);
        }
        Employer result = employerRepository.save(employer);
        return ResponseEntity.created(new URI("/api/employers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("employer", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /employers -> Updates an existing employer.
     */
    @RequestMapping(value = "/employers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employer> updateEmployer(@RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to update Employer : {}", employer);
        if (employer.getId() == null) {
            return createEmployer(employer);
        }
        Employer result = employerRepository.save(employer);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("employer", employer.getId().toString()))
                .body(result);
    }

    /**
     * GET  /employers -> get all the employers.
     */
    @RequestMapping(value = "/employers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Employer>> getAllEmployers(Pageable pageable)
        throws URISyntaxException {
        Page<Employer> page = employerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employers/:id -> get the "id" employer.
     */
    @RequestMapping(value = "/employers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employer> getEmployer(@PathVariable Long id) {
        log.debug("REST request to get Employer : {}", id);
        return Optional.ofNullable(employerRepository.findOne(id))
            .map(employer -> new ResponseEntity<>(
                employer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employers/:id -> delete the "id" employer.
     */
    @RequestMapping(value = "/employers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long id) {
        log.debug("REST request to delete Employer : {}", id);
        employerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employer", id.toString())).build();
    }
}
