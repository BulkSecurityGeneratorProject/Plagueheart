package au.com.iglooit.searchcloud.repository.search;

import au.com.iglooit.searchcloud.domain.api.PDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by nicholaszhu on 16/07/2016.
 */
public interface PDocumentSearchRepository extends ElasticsearchRepository<PDocument, Long> {
}
