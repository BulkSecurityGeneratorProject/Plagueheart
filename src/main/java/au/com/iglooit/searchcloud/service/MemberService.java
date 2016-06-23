package au.com.iglooit.searchcloud.service;

import au.com.iglooit.searchcloud.domain.Member;
import au.com.iglooit.searchcloud.repository.MemberRepository;
import au.com.iglooit.searchcloud.repository.search.MemberSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Member.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);
    
    @Inject
    private MemberRepository memberRepository;
    
    @Inject
    private MemberSearchRepository memberSearchRepository;
    
    /**
     * Save a member.
     * @return the persisted entity
     */
    public Member save(Member member) {
        log.debug("Request to save Member : {}", member);
        Member result = memberRepository.save(member);
        memberSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the members.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Member> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        Page<Member> result = memberRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one member by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Member findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        Member member = memberRepository.findOne(id);
        return member;
    }

    /**
     *  delete the  member by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.delete(id);
        memberSearchRepository.delete(id);
    }

    /**
     * search for the member corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Member> search(String query) {
        
        log.debug("REST request to search Members for query {}", query);
        return StreamSupport
            .stream(memberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * search member based on the company
     * @param companyID companyID
     * @param query query string
     * @return list of members
     */
    @Transactional(readOnly = true)
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
