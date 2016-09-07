package com.ticketing.repository.search;

import com.ticketing.domain.Airport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Airport entity.
 */
public interface AirportSearchRepository extends ElasticsearchRepository<Airport, Long> {
}
