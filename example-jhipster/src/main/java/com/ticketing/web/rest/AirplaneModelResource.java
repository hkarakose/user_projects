package com.ticketing.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ticketing.domain.AirplaneModel;

import com.ticketing.repository.AirplaneModelRepository;
import com.ticketing.repository.search.AirplaneModelSearchRepository;
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
 * REST controller for managing AirplaneModel.
 */
@RestController
@RequestMapping("/api")
public class AirplaneModelResource {

    private final Logger log = LoggerFactory.getLogger(AirplaneModelResource.class);
        
    @Inject
    private AirplaneModelRepository airplaneModelRepository;

    @Inject
    private AirplaneModelSearchRepository airplaneModelSearchRepository;

    /**
     * POST  /airplane-models : Create a new airplaneModel.
     *
     * @param airplaneModel the airplaneModel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new airplaneModel, or with status 400 (Bad Request) if the airplaneModel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplane-models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModel> createAirplaneModel(@Valid @RequestBody AirplaneModel airplaneModel) throws URISyntaxException {
        log.debug("REST request to save AirplaneModel : {}", airplaneModel);
        if (airplaneModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("airplaneModel", "idexists", "A new airplaneModel cannot already have an ID")).body(null);
        }
        AirplaneModel result = airplaneModelRepository.save(airplaneModel);
        airplaneModelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/airplane-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("airplaneModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /airplane-models : Updates an existing airplaneModel.
     *
     * @param airplaneModel the airplaneModel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated airplaneModel,
     * or with status 400 (Bad Request) if the airplaneModel is not valid,
     * or with status 500 (Internal Server Error) if the airplaneModel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/airplane-models",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModel> updateAirplaneModel(@Valid @RequestBody AirplaneModel airplaneModel) throws URISyntaxException {
        log.debug("REST request to update AirplaneModel : {}", airplaneModel);
        if (airplaneModel.getId() == null) {
            return createAirplaneModel(airplaneModel);
        }
        AirplaneModel result = airplaneModelRepository.save(airplaneModel);
        airplaneModelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("airplaneModel", airplaneModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /airplane-models : get all the airplaneModels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of airplaneModels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/airplane-models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AirplaneModel>> getAllAirplaneModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AirplaneModels");
        Page<AirplaneModel> page = airplaneModelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/airplane-models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /airplane-models/:id : get the "id" airplaneModel.
     *
     * @param id the id of the airplaneModel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the airplaneModel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/airplane-models/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AirplaneModel> getAirplaneModel(@PathVariable Long id) {
        log.debug("REST request to get AirplaneModel : {}", id);
        AirplaneModel airplaneModel = airplaneModelRepository.findOne(id);
        return Optional.ofNullable(airplaneModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /airplane-models/:id : delete the "id" airplaneModel.
     *
     * @param id the id of the airplaneModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/airplane-models/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAirplaneModel(@PathVariable Long id) {
        log.debug("REST request to delete AirplaneModel : {}", id);
        airplaneModelRepository.delete(id);
        airplaneModelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("airplaneModel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/airplane-models?query=:query : search for the airplaneModel corresponding
     * to the query.
     *
     * @param query the query of the airplaneModel search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/airplane-models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AirplaneModel>> searchAirplaneModels(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AirplaneModels for query {}", query);
        Page<AirplaneModel> page = airplaneModelSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/airplane-models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
