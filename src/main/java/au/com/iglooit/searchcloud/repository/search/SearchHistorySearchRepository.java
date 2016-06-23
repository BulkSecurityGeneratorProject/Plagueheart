package au.com.iglooit.searchcloud.repository.search;

import au.com.iglooit.searchcloud.domain.SearchHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SearchHistory entity.
 */
public interface SearchHistorySearchRepository extends ElasticsearchRepository<SearchHistory, Long> {
}
