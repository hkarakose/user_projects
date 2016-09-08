package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.Flight;
import com.ticketing.domain.FlightSeat;
import com.ticketing.domain.User;
import com.ticketing.repository.FlightRepository;
import com.ticketing.repository.FlightSeatRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.repository.search.FlightSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Flight.
 */
@RestController
@RequestMapping("/api")
public class FlightReservationResource {

    private final Logger log = LoggerFactory.getLogger(FlightReservationResource.class);

    @Inject
    private FlightRepository flightRepository;

    @Inject
    private FlightSeatRepository flightSeatRepository;

    @Inject
    private FlightSearchRepository flightSearchRepository;

    @Inject
    UserRepository userRepository;

    /**
     * POST  /flights : Create a new flight.
     *
     * @param flightId the flight to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flight, or with status 400 (Bad Request) if the flight has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reservation/{flightId}/{username}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlightSeat> makeReservation(@PathVariable Long flightId, @PathVariable String username) throws URISyntaxException {
        log.debug("REST request to make reservation for Flight : {}", flightId);
//        if (flight.getId() == null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flight", "iddoesnotexist", "A new reservation cannot be completed without a flight ID")).body(null);
//        }

        List<FlightSeat> byFlight = flightSeatRepository.findByFlightIdAndAvailability(flightId, true);
        if (byFlight.size() == 0) {
            return null;
        }

        FlightSeat flightSeat = byFlight.get(0);
        flightSeat.setAvailability(false);
        User user = userRepository.findOneByLogin(username).get();
        flightSeat.setOwner(user);

        FlightSeat flightSeat1 = flightSeatRepository.save(flightSeat);

        return Optional.ofNullable(flightSeat1)
            .map(result -> new ResponseEntity<>(
                flightSeat1,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /flights : get all the flights.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of flights in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/reservation",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Flight>> getAllFlights(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Flights");
        Page<Flight> page = flightRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flights");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flights/:id : get the "id" flight.
     *
     * @param id the id of the flight to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flight, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/reservation/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        log.debug("REST request to get Flight : {}", id);
        Flight flight = flightRepository.findOne(id);
        return Optional.ofNullable(flight)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flights/:id : delete the "id" flight.
     *
     * @param id the id of the flight to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/reservation/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        log.debug("REST request to delete Flight : {}", id);
        flightRepository.delete(id);
        flightSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flight", id.toString())).build();
    }

    /**
     * SEARCH  /_search/flights?query=:query : search for the flight corresponding
     * to the query.
     *
     * @param query the query of the flight search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/reservation",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Flight>> searchFlights(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Flights for query {}", query);
        Page<Flight> page = flightSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/flights");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
