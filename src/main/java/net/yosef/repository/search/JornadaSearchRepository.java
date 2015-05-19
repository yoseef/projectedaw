package net.yosef.repository.search;

import net.yosef.domain.Jornada;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Jornada entity.
 */
public interface JornadaSearchRepository extends ElasticsearchRepository<Jornada, Long> {
}
