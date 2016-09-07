package com.ticketing.repository.search;

import com.ticketing.domain.Airlines;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Airlines entity.
 */
public interface AirlinesSearchRepository extends ElasticsearchRepository<Airlines, Long> {
}
