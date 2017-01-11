package com.ticketing.repository.search;

import com.ticketing.domain.Flight;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Flight entity.
 */
public interface FlightSearchRepository extends ElasticsearchRepository<Flight, Long> {
}
