package com.ticketing.repository.search;

import com.ticketing.domain.AirplaneModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AirplaneModel entity.
 */
public interface AirplaneModelSearchRepository extends ElasticsearchRepository<AirplaneModel, Long> {
}
