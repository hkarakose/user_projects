package com.ticketing.repository.search;

import com.ticketing.domain.City;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the City entity.
 */
public interface CitySearchRepository extends ElasticsearchRepository<City, Long> {
}
