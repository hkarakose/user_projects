package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.Airlines;

import com.ticketing.repository.AirlinesRepository;
import com.ticketing.repository.search.AirlinesSearchRepository;
import com.ticketing.web.rest.util.HeaderUtil;
import com.ticketing.web.rest.util.PaginationUtil;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Airlines.
 */
@RestController
@RequestMapping("/api")
public class AirlinesResource {

    private final Logger log = LoggerFactory.getLogger(AirlinesResource.class);
        
    @Inject
    private AirlinesRepository airlinesRepository;

    @Inject
    private AirlinesSearchRepository airlinesSearchRepository;

    /**
     * POST  /airlines : Create a new airlines.
     *
     * @param airlines the airlines to create
     * @return the ResponseEntity with status 201 (Created) and with body the new airlines, or with status 400 (Bad Request) if the airlines has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airlines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airlines> createAirlines(@Valid @RequestBody Airlines airlines) throws URISyntaxException {
        log.debug("REST request to save Airlines : {}", airlines);
        if (airlines.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("airlines", "idexists", "A new airlines cannot already have an ID")).body(null);
        }
        Airlines result = airlinesRepository.save(airlines);
        airlinesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/airlines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("airlines", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /airlines : Updates an existing airlines.
     *
     * @param airlines the airlines to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated airlines,
     * or with status 400 (Bad Request) if the airlines is not valid,
     * or with status 500 (Internal Server Error) if the airlines couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airlines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airlines> updateAirlines(@Valid @RequestBody Airlines airlines) throws URISyntaxException {
        log.debug("REST request to update Airlines : {}", airlines);
        if (airlines.getId() == null) {
            return createAirlines(airlines);
        }
        Airlines result = airlinesRepository.save(airlines);
        airlinesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("airlines", airlines.getId().toString()))
            .body(result);
    }

    /**
     * GET  /airlines : get all the airlines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of airlines in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/airlines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airlines>> getAllAirlines(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Airlines");
        Page<Airlines> page = airlinesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/airlines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /airlines/:id : get the "id" airlines.
     *
     * @param id the id of the airlines to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the airlines, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/airlines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airlines> getAirlines(@PathVariable Long id) {
        log.debug("REST request to get Airlines : {}", id);
        Airlines airlines = airlinesRepository.findOne(id);
        return Optional.ofNullable(airlines)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /airlines/:id : delete the "id" airlines.
     *
     * @param id the id of the airlines to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/airlines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAirlines(@PathVariable Long id) {
        log.debug("REST request to delete Airlines : {}", id);
        airlinesRepository.delete(id);
        airlinesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("airlines", id.toString())).build();
    }

    /**
     * SEARCH  /_search/airlines?query=:query : search for the airlines corresponding
     * to the query.
     *
     * @param query the query of the airlines search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/airlines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airlines>> searchAirlines(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Airlines for query {}", query);
        Page<Airlines> page = airlinesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/airlines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
