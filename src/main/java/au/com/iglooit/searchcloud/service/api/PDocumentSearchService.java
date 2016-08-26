package au.com.iglooit.searchcloud.service.api;


import au.com.iglooit.searchcloud.domain.api.PDocument;
import au.com.iglooit.searchcloud.repository.search.PDocumentSearchRepository;
import au.com.iglooit.searchcloud.util.PDocumentUtil;
import au.com.iglooit.searchcloud.web.rest.dto.PDocumentDTO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    public PDocumentDTO saveDocumentToCloud(PDocument pDocument) {
        log.debug("save document: {}", pDocument);
        return new PDocumentDTO(pDocumentSearchRepository.save(pDocument));
    }

    public Boolean deleteDocument(String docId) {
        log.debug("delete document via docId: {}", docId);
        PDocument document = loadDocumentByDocId(docId);
        if (document != null) {
            pDocumentSearchRepository.delete(document.getId());
            return true;
        }
        return false;
    }

    public List<PDocumentDTO> searchByKey(String query) {
        log.debug("REST request to search PDocument for query {}", query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(queryStringQuery(query));
        return StreamSupport
                .stream(pDocumentSearchRepository.search(queryBuilders).spliterator(), false)
                .map(PDocumentDTO::new)
                .collect(Collectors.toList());
    }

    public List<PDocumentDTO> search(String query) {
//        log.debug("REST request to search PDocument for query {}", query);
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.wrapperQuery(query))
//                .withHighlightFields(new HighlightBuilder.Field("file"))
//                .build();
//
//        return StreamSupport
//                .stream(pDocumentSearchRepository.search(searchQuery).spliterator(), false)
//                .map(PDocumentDTO::new)
//                .collect(Collectors.toList());
        return nativeSearch(query);
    }

    public List<PDocumentDTO> search(String companyId, String query) {
        log.debug("REST request to search PDocument for company {} and query {}", companyId, query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("companyId", companyId))
                .must(queryStringQuery(query));
        return StreamSupport
                .stream(pDocumentSearchRepository.search(queryBuilders).spliterator(), false)
                .map(PDocumentDTO::new)
                .collect(Collectors.toList());
    }

    public PDocument loadDocument(Long id) {
        log.debug("REST request to load PDocument for id {}", id);
        return pDocumentSearchRepository.findOne(id);
    }

    public PDocument loadDocumentByDocId(String docId) {
        log.debug("REST request to search PDocument for docId {}", docId);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("docId", docId));
        Iterator<PDocument> result = pDocumentSearchRepository.search(queryBuilders).iterator();
        if (result.hasNext()) {
            return result.next();
        } else {
            return null;
        }
    }

    private List<PDocumentDTO> nativeSearch(String query) {
        log.debug("REST request to search PDocument for query {}", query);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.wrapperQuery(query))
                .withHighlightFields(new HighlightBuilder.Field("file"))
                .build();
        FacetedPage<PDocument> rawResult = elasticsearchTemplate.queryForPage(searchQuery, PDocument.class,
                new SearchResultMapper() {
                    @Override
                    public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                        List<PDocument> chunk = new ArrayList<PDocument>();
                        for (SearchHit searchHit : response.getHits()) {
                            if (response.getHits().getHits().length <= 0) {
                                return null;
                            }
                            PDocument document = new PDocument();
                            document.setId(Long.valueOf((Integer) searchHit.getSource().get("id")));
                            document.setCompanyId((Integer) searchHit.getSource().get("companyId"));
                            document.setApiKey((String) searchHit.getSource().get("apiKey"));
                            document.setAppMeta((String) searchHit.getSource().get("appMeta"));
                            document.setCreatedBy((String) searchHit.getSource().get("createdBy"));
                            document.setCreatedDateTime(PDocumentUtil.parseLocalDate((String) searchHit.getSource().get
                                    ("createdDateTime")));
                            document.setDocId((Integer) searchHit.getSource().get("docId"));
                            document.setFileName((String) searchHit.getSource().get("fileName"));
                            document.setTitle((String) searchHit.getSource().get("title"));
                            document.setFileSize((String) searchHit.getSource().get("fileSize"));
                            document.setFile((String) searchHit.getSource().get("file"));

                            if (searchHit.getHighlightFields().get("file") != null &&
                                    searchHit.getHighlightFields().get("file").fragments() != null && searchHit
                                    .getHighlightFields().get("file").fragments().length > 0) {
                                document.setHighLightMessage(searchHit.getHighlightFields().get("file").fragments()[0]
                                        .toString());
                            }

                            document.setTags((List<Integer>) searchHit.getSource().get("tags"));
                            chunk.add(document);
                        }
                        if (chunk.size() > 0) {
                            return new FacetedPageImpl<T>((List<T>) chunk);
                        }
                        return null;
                    }
                });
        if (rawResult != null) {
            return StreamSupport
                    .stream(rawResult.spliterator(), false)
                    .map(PDocumentDTO::new)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<PDocumentDTO>();
        }
    }
}
