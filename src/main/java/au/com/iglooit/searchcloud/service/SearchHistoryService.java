package au.com.iglooit.searchcloud.service;

import au.com.iglooit.searchcloud.domain.SearchHistory;
import au.com.iglooit.searchcloud.repository.SearchHistoryRepository;
import au.com.iglooit.searchcloud.repository.search.SearchHistorySearchRepository;
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
 * Service Implementation for managing SearchHistory.
 */
@Service
@Transactional
public class SearchHistoryService {

    private final Logger log = LoggerFactory.getLogger(SearchHistoryService.class);
    
    @Inject
    private SearchHistoryRepository searchHistoryRepository;
    
    @Inject
    private SearchHistorySearchRepository searchHistorySearchRepository;
    
    /**
     * Save a searchHistory.
     * @return the persisted entity
     */
    public SearchHistory save(SearchHistory searchHistory) {
        log.debug("Request to save SearchHistory : {}", searchHistory);
        SearchHistory result = searchHistoryRepository.save(searchHistory);
        searchHistorySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the searchHistorys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SearchHistory> findAll(Pageable pageable) {
        log.debug("Request to get all SearchHistorys");
        Page<SearchHistory> result = searchHistoryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one searchHistory by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SearchHistory findOne(Long id) {
        log.debug("Request to get SearchHistory : {}", id);
        SearchHistory searchHistory = searchHistoryRepository.findOne(id);
        return searchHistory;
    }

    /**
     *  delete the  searchHistory by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchHistory : {}", id);
        searchHistoryRepository.delete(id);
        searchHistorySearchRepository.delete(id);
    }

    /**
     * search for the searchHistory corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<SearchHistory> search(String query) {
        
        log.debug("REST request to search SearchHistorys for query {}", query);
        return StreamSupport
            .stream(searchHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
