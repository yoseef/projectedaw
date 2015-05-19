package net.yosef.repository.search;

import net.yosef.domain.Franja;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Franja entity.
 */
public interface FranjaSearchRepository extends ElasticsearchRepository<Franja, Long> {
}
