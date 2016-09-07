package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.Airplane;

import com.ticketing.repository.AirplaneRepository;
import com.ticketing.repository.search.AirplaneSearchRepository;
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
 * REST controller for managing Airplane.
 */
@RestController
@RequestMapping("/api")
public class AirplaneResource {

    private final Logger log = LoggerFactory.getLogger(AirplaneResource.class);
        
    @Inject
    private AirplaneRepository airplaneRepository;

    @Inject
    private AirplaneSearchRepository airplaneSearchRepository;

    /**
     * POST  /airplanes : Create a new airplane.
     *
     * @param airplane the airplane to create
     * @return the ResponseEntity with status 201 (Created) and with body the new airplane, or with status 400 (Bad Request) if the airplane has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplanes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airplane> createAirplane(@Valid @RequestBody Airplane airplane) throws URISyntaxException {
        log.debug("REST request to save Airplane : {}", airplane);
        if (airplane.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("airplane", "idexists", "A new airplane cannot already have an ID")).body(null);
        }
        Airplane result = airplaneRepository.save(airplane);
        airplaneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/airplanes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("airplane", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /airplanes : Updates an existing airplane.
     *
     * @param airplane the airplane to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated airplane,
     * or with status 400 (Bad Request) if the airplane is not valid,
     * or with status 500 (Internal Server Error) if the airplane couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplanes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airplane> updateAirplane(@Valid @RequestBody Airplane airplane) throws URISyntaxException {
        log.debug("REST request to update Airplane : {}", airplane);
        if (airplane.getId() == null) {
            return createAirplane(airplane);
        }
        Airplane result = airplaneRepository.save(airplane);
        airplaneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("airplane", airplane.getId().toString()))
            .body(result);
    }

    /**
     * GET  /airplanes : get all the airplanes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of airplanes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/airplanes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airplane>> getAllAirplanes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Airplanes");
        Page<Airplane> page = airplaneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/airplanes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /airplanes/:id : get the "id" airplane.
     *
     * @param id the id of the airplane to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the airplane, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/airplanes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airplane> getAirplane(@PathVariable Long id) {
        log.debug("REST request to get Airplane : {}", id);
        Airplane airplane = airplaneRepository.findOne(id);
        return Optional.ofNullable(airplane)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /airplanes/:id : delete the "id" airplane.
     *
     * @param id the id of the airplane to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/airplanes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAirplane(@PathVariable Long id) {
        log.debug("REST request to delete Airplane : {}", id);
        airplaneRepository.delete(id);
        airplaneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("airplane", id.toString())).build();
    }

    /**
     * SEARCH  /_search/airplanes?query=:query : search for the airplane corresponding
     * to the query.
     *
     * @param query the query of the airplane search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/airplanes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airplane>> searchAirplanes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Airplanes for query {}", query);
        Page<Airplane> page = airplaneSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/airplanes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
