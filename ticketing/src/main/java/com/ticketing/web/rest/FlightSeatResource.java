package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.FlightSeat;

import com.ticketing.repository.FlightSeatRepository;
import com.ticketing.repository.search.FlightSeatSearchRepository;
import com.ticketing.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing FlightSeat.
 */
@RestController
@RequestMapping("/api")
public class FlightSeatResource {

    private final Logger log = LoggerFactory.getLogger(FlightSeatResource.class);
        
    @Inject
    private FlightSeatRepository flightSeatRepository;

    @Inject
    private FlightSeatSearchRepository flightSeatSearchRepository;

    /**
     * POST  /flight-seats : Create a new flightSeat.
     *
     * @param flightSeat the flightSeat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flightSeat, or with status 400 (Bad Request) if the flightSeat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flight-seats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSeat> createFlightSeat(@Valid @RequestBody FlightSeat flightSeat) throws URISyntaxException {
        log.debug("REST request to save FlightSeat : {}", flightSeat);
        if (flightSeat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flightSeat", "idexists", "A new flightSeat cannot already have an ID")).body(null);
        }
        FlightSeat result = flightSeatRepository.save(flightSeat);
        flightSeatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/flight-seats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flightSeat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flight-seats : Updates an existing flightSeat.
     *
     * @param flightSeat the flightSeat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flightSeat,
     * or with status 400 (Bad Request) if the flightSeat is not valid,
     * or with status 500 (Internal Server Error) if the flightSeat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flight-seats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSeat> updateFlightSeat(@Valid @RequestBody FlightSeat flightSeat) throws URISyntaxException {
        log.debug("REST request to update FlightSeat : {}", flightSeat);
        if (flightSeat.getId() == null) {
            return createFlightSeat(flightSeat);
        }
        FlightSeat result = flightSeatRepository.save(flightSeat);
        flightSeatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flightSeat", flightSeat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flight-seats : get all the flightSeats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of flightSeats in body
     */
    @RequestMapping(value = "/flight-seats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlightSeat> getAllFlightSeats() {
        log.debug("REST request to get all FlightSeats");
        List<FlightSeat> flightSeats = flightSeatRepository.findAll();
        return flightSeats;
    }

    /**
     * GET  /flight-seats/:id : get the "id" flightSeat.
     *
     * @param id the id of the flightSeat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flightSeat, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/flight-seats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSeat> getFlightSeat(@PathVariable Long id) {
        log.debug("REST request to get FlightSeat : {}", id);
        FlightSeat flightSeat = flightSeatRepository.findOne(id);
        return Optional.ofNullable(flightSeat)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flight-seats/:id : delete the "id" flightSeat.
     *
     * @param id the id of the flightSeat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/flight-seats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlightSeat(@PathVariable Long id) {
        log.debug("REST request to delete FlightSeat : {}", id);
        flightSeatRepository.delete(id);
        flightSeatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flightSeat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/flight-seats?query=:query : search for the flightSeat corresponding
     * to the query.
     *
     * @param query the query of the flightSeat search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/flight-seats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlightSeat> searchFlightSeats(@RequestParam String query) {
        log.debug("REST request to search FlightSeats for query {}", query);
        return StreamSupport
            .stream(flightSeatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
