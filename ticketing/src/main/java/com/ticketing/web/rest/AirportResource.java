package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.Airport;

import com.ticketing.repository.AirportRepository;
import com.ticketing.repository.search.AirportSearchRepository;
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
 * REST controller for managing Airport.
 */
@RestController
@RequestMapping("/api")
public class AirportResource {

    private final Logger log = LoggerFactory.getLogger(AirportResource.class);
        
    @Inject
    private AirportRepository airportRepository;

    @Inject
    private AirportSearchRepository airportSearchRepository;

    /**
     * POST  /airports : Create a new airport.
     *
     * @param airport the airport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new airport, or with status 400 (Bad Request) if the airport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airports",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airport> createAirport(@Valid @RequestBody Airport airport) throws URISyntaxException {
        log.debug("REST request to save Airport : {}", airport);
        if (airport.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("airport", "idexists", "A new airport cannot already have an ID")).body(null);
        }
        Airport result = airportRepository.save(airport);
        airportSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/airports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("airport", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /airports : Updates an existing airport.
     *
     * @param airport the airport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated airport,
     * or with status 400 (Bad Request) if the airport is not valid,
     * or with status 500 (Internal Server Error) if the airport couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airports",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airport> updateAirport(@Valid @RequestBody Airport airport) throws URISyntaxException {
        log.debug("REST request to update Airport : {}", airport);
        if (airport.getId() == null) {
            return createAirport(airport);
        }
        Airport result = airportRepository.save(airport);
        airportSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("airport", airport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /airports : get all the airports.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of airports in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/airports",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airport>> getAllAirports(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Airports");
        Page<Airport> page = airportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/airports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /airports/:id : get the "id" airport.
     *
     * @param id the id of the airport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the airport, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/airports/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Airport> getAirport(@PathVariable Long id) {
        log.debug("REST request to get Airport : {}", id);
        Airport airport = airportRepository.findOne(id);
        return Optional.ofNullable(airport)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /airports/:id : delete the "id" airport.
     *
     * @param id the id of the airport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/airports/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        log.debug("REST request to delete Airport : {}", id);
        airportRepository.delete(id);
        airportSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("airport", id.toString())).build();
    }

    /**
     * SEARCH  /_search/airports?query=:query : search for the airport corresponding
     * to the query.
     *
     * @param query the query of the airport search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/airports",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Airport>> searchAirports(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Airports for query {}", query);
        Page<Airport> page = airportSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/airports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
