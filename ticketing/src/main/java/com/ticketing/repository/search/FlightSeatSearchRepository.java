package com.ticketing.repository.search;

import com.ticketing.domain.FlightSeat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FlightSeat entity.
 */
public interface FlightSeatSearchRepository extends ElasticsearchRepository<FlightSeat, Long> {
}
