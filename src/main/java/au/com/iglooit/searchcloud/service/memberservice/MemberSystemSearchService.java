package au.com.iglooit.searchcloud.service.memberservice;

import au.com.iglooit.searchcloud.domain.Member;
import au.com.iglooit.searchcloud.domain.SearchHistory;
import au.com.iglooit.searchcloud.repository.search.MemberSearchRepository;
import au.com.iglooit.searchcloud.service.SearchHistoryService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by nicholaszhu on 23/06/2016.
 */
@Service
@Transactional(readOnly = true)
public class MemberSystemSearchService {
    private final Logger log = LoggerFactory.getLogger(MemberSystemSearchService.class);
    @Inject
    private MemberSearchRepository memberSearchRepository;

    /**
     * search member based on the company
     * @param companyID companyID
     * @param query query string
     * @return list of members
     */
    public List<Member> search(String companyID, String query){
        log.debug("REST request to search Members for company {} and query {}", companyID, query);
        QueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("company.id", companyID))
                .must(queryStringQuery(query));
        return StreamSupport
                .stream(memberSearchRepository.search(queryBuilders).spliterator(), false)
                .collect(Collectors.toList());
    }
}
