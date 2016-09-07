package com.ticketing.repository.search;

import com.ticketing.domain.Airplane;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Airplane entity.
 */
public interface AirplaneSearchRepository extends ElasticsearchRepository<Airplane, Long> {
}
