package com.ticketing.repository.search;

import com.ticketing.domain.AirplaneModelSeat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AirplaneModelSeat entity.
 */
public interface AirplaneModelSeatSearchRepository extends ElasticsearchRepository<AirplaneModelSeat, Long> {
}
