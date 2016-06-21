package au.com.iglooit.searchcloud.repository.search;

import au.com.iglooit.searchcloud.domain.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Member entity.
 */
public interface MemberSearchRepository extends ElasticsearchRepository<Member, Long> {
}
