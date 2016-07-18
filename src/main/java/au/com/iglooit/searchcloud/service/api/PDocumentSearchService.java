package au.com.iglooit.searchcloud.service.api;


import au.com.iglooit.searchcloud.domain.api.PDocument;
import au.com.iglooit.searchcloud.repository.search.PDocumentSearchRepository;
import au.com.iglooit.searchcloud.web.rest.dto.PDocumentDTO;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@Transactional
public class PDocumentSearchService {
    private final Logger log = LoggerFactory.getLogger(PDocumentSearchService.class);

    @Inject
    private PDocumentSearchRepository pDocumentSearchRepository;

    public PDocumentDTO saveDocumentToCloud(PDocument pDocument) {
        log.debug("save document: {}", pDocument);
        return new PDocumentDTO(pDocumentSearchRepository.save(pDocument));
    }

    public List<PDocumentDTO> search(String query){
        log.debug("REST request to search PDocument for query {}", query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(queryStringQuery(query));
        return StreamSupport
                .stream(pDocumentSearchRepository.search(queryBuilders).spliterator(), false)
                .map(PDocumentDTO::new)
                .collect(Collectors.toList());
    }

    public List<PDocumentDTO> search(String companyId, String query){
        log.debug("REST request to search PDocument for company {} and query {}", companyId, query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("companyId", companyId))
                .must(queryStringQuery(query));
        return StreamSupport
                .stream(pDocumentSearchRepository.search(queryBuilders).spliterator(), false)
                .map(PDocumentDTO::new)
                .collect(Collectors.toList());
    }

    public PDocument loadDocument(Long id){
        log.debug("REST request to load PDocument for id {}", id);
        return pDocumentSearchRepository.findOne(id);
    }
}
