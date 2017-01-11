package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.AirplaneModelSeat;

import com.ticketing.repository.AirplaneModelSeatRepository;
import com.ticketing.repository.search.AirplaneModelSeatSearchRepository;
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
 * REST controller for managing AirplaneModelSeat.
 */
@RestController
@RequestMapping("/api")
public class AirplaneModelSeatResource {

    private final Logger log = LoggerFactory.getLogger(AirplaneModelSeatResource.class);
        
    @Inject
    private AirplaneModelSeatRepository airplaneModelSeatRepository;

    @Inject
    private AirplaneModelSeatSearchRepository airplaneModelSeatSearchRepository;

    /**
     * POST  /airplane-model-seats : Create a new airplaneModelSeat.
     *
     * @param airplaneModelSeat the airplaneModelSeat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new airplaneModelSeat, or with status 400 (Bad Request) if the airplaneModelSeat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplane-model-seats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModelSeat> createAirplaneModelSeat(@Valid @RequestBody AirplaneModelSeat airplaneModelSeat) throws URISyntaxException {
        log.debug("REST request to save AirplaneModelSeat : {}", airplaneModelSeat);
        if (airplaneModelSeat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("airplaneModelSeat", "idexists", "A new airplaneModelSeat cannot already have an ID")).body(null);
        }
        AirplaneModelSeat result = airplaneModelSeatRepository.save(airplaneModelSeat);
        airplaneModelSeatSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/airplane-model-seats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("airplaneModelSeat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /airplane-model-seats : Updates an existing airplaneModelSeat.
     *
     * @param airplaneModelSeat the airplaneModelSeat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated airplaneModelSeat,
     * or with status 400 (Bad Request) if the airplaneModelSeat is not valid,
     * or with status 500 (Internal Server Error) if the airplaneModelSeat couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplane-model-seats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModelSeat> updateAirplaneModelSeat(@Valid @RequestBody AirplaneModelSeat airplaneModelSeat) throws URISyntaxException {
        log.debug("REST request to update AirplaneModelSeat : {}", airplaneModelSeat);
        if (airplaneModelSeat.getId() == null) {
            return createAirplaneModelSeat(airplaneModelSeat);
        }
        AirplaneModelSeat result = airplaneModelSeatRepository.save(airplaneModelSeat);
        airplaneModelSeatSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("airplaneModelSeat", airplaneModelSeat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /airplane-model-seats : get all the airplaneModelSeats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of airplaneModelSeats in body
     */
    @RequestMapping(value = "/airplane-model-seats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AirplaneModelSeat> getAllAirplaneModelSeats() {
        log.debug("REST request to get all AirplaneModelSeats");
        List<AirplaneModelSeat> airplaneModelSeats = airplaneModelSeatRepository.findAll();
        return airplaneModelSeats;
    }

    /**
     * GET  /airplane-model-seats/:id : get the "id" airplaneModelSeat.
     *
     * @param id the id of the airplaneModelSeat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the airplaneModelSeat, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/airplane-model-seats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModelSeat> getAirplaneModelSeat(@PathVariable Long id) {
        log.debug("REST request to get AirplaneModelSeat : {}", id);
        AirplaneModelSeat airplaneModelSeat = airplaneModelSeatRepository.findOne(id);
        return Optional.ofNullable(airplaneModelSeat)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /airplane-model-seats/:id : delete the "id" airplaneModelSeat.
     *
     * @param id the id of the airplaneModelSeat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/airplane-model-seats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAirplaneModelSeat(@PathVariable Long id) {
        log.debug("REST request to delete AirplaneModelSeat : {}", id);
        airplaneModelSeatRepository.delete(id);
        airplaneModelSeatSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("airplaneModelSeat", id.toString())).build();
    }

    /**
     * SEARCH  /_search/airplane-model-seats?query=:query : search for the airplaneModelSeat corresponding
     * to the query.
     *
     * @param query the query of the airplaneModelSeat search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/airplane-model-seats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AirplaneModelSeat> searchAirplaneModelSeats(@RequestParam String query) {
        log.debug("REST request to search AirplaneModelSeats for query {}", query);
        return StreamSupport
            .stream(airplaneModelSeatSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
