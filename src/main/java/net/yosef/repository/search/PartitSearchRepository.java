package net.yosef.repository.search;

import net.yosef.domain.Partit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Partit entity.
 */
public interface PartitSearchRepository extends ElasticsearchRepository<Partit, Long> {
}
