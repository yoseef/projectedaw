package net.yosef.repository.search;

import net.yosef.domain.Grup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Grup entity.
 */
public interface GrupSearchRepository extends ElasticsearchRepository<Grup, Long> {
}
