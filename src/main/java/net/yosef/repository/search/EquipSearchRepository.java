package net.yosef.repository.search;

import net.yosef.domain.Equip;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Equip entity.
 */
public interface EquipSearchRepository extends ElasticsearchRepository<Equip, Long> {
}
