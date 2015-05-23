package net.yosef.repository.search;

import net.yosef.domain.Classificacio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Classificacio entity.
 */
public interface ClassificacioSearchRepository extends ElasticsearchRepository<Classificacio, Long> {
}
