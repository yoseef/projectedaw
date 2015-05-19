package net.yosef.repository.search;

import net.yosef.domain.Temporada;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Temporada entity.
 */
public interface TemporadaSearchRepository extends ElasticsearchRepository<Temporada, Long> {
}
