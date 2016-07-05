package au.com.iglooit.searchcloud.service.search;

import au.com.iglooit.searchcloud.config.elasticsearch.ElasticSearchWrapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by nicholaszhu on 29/06/2016.
 */
@Service
public class IndexDataService {
    private final Logger log = LoggerFactory.getLogger(IndexDataService.class);
    private final static String INDEX_DATA = "indexdata";
    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;
    @Inject
    private ElasticSearchWrapper elasticSearchWrapper;

    public String createIndexData(final String type, final String jsonString) {
        return elasticsearchTemplate.index(new IndexQuery() {{
            setIndexName(INDEX_DATA);
            setSource(jsonString);
            setType(type);
        }});
    }

    public String updateIndexData(final String id, final String type, final String jsonString) {
        return elasticsearchTemplate.index(new IndexQuery() {{
            setId(id);
            setIndexName(INDEX_DATA);
            setSource(jsonString);
            setType(type);
        }});
    }

    public boolean updateIndexMap(final String type, final Object mapping) {
        return elasticsearchTemplate.putMapping(INDEX_DATA, type, mapping);
    }

    public SearchResponse search(String type, String query) {
        log.debug("REST request to search Members for type {} and query {}", type, query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(queryStringQuery(query));
        return elasticSearchWrapper
                .getClient()
                .prepareSearch(INDEX_DATA)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilders)
                .setFrom(0).setSize(10).setExplain(true)
                .execute()
                .actionGet();
    }


}
