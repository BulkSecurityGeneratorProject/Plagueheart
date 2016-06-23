package au.com.iglooit.searchcloud.web.rest;

import com.codahale.metrics.annotation.Timed;
import au.com.iglooit.searchcloud.domain.SearchHistory;
import au.com.iglooit.searchcloud.service.SearchHistoryService;
import au.com.iglooit.searchcloud.web.rest.util.HeaderUtil;
import au.com.iglooit.searchcloud.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SearchHistory.
 */
@RestController
@RequestMapping("/api")
public class SearchHistoryResource {

    private final Logger log = LoggerFactory.getLogger(SearchHistoryResource.class);
        
    @Inject
    private SearchHistoryService searchHistoryService;
    
    /**
     * POST  /searchHistorys -> Create a new searchHistory.
     */
    @RequestMapping(value = "/searchHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SearchHistory> createSearchHistory(@RequestBody SearchHistory searchHistory) throws URISyntaxException {
        log.debug("REST request to save SearchHistory : {}", searchHistory);
        if (searchHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("searchHistory", "idexists", "A new searchHistory cannot already have an ID")).body(null);
        }
        SearchHistory result = searchHistoryService.save(searchHistory);
        return ResponseEntity.created(new URI("/api/searchHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("searchHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /searchHistorys -> Updates an existing searchHistory.
     */
    @RequestMapping(value = "/searchHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SearchHistory> updateSearchHistory(@RequestBody SearchHistory searchHistory) throws URISyntaxException {
        log.debug("REST request to update SearchHistory : {}", searchHistory);
        if (searchHistory.getId() == null) {
            return createSearchHistory(searchHistory);
        }
        SearchHistory result = searchHistoryService.save(searchHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("searchHistory", searchHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /searchHistorys -> get all the searchHistorys.
     */
    @RequestMapping(value = "/searchHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SearchHistory>> getAllSearchHistorys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SearchHistorys");
        Page<SearchHistory> page = searchHistoryService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/searchHistorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /searchHistorys/:id -> get the "id" searchHistory.
     */
    @RequestMapping(value = "/searchHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SearchHistory> getSearchHistory(@PathVariable Long id) {
        log.debug("REST request to get SearchHistory : {}", id);
        SearchHistory searchHistory = searchHistoryService.findOne(id);
        return Optional.ofNullable(searchHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /searchHistorys/:id -> delete the "id" searchHistory.
     */
    @RequestMapping(value = "/searchHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSearchHistory(@PathVariable Long id) {
        log.debug("REST request to delete SearchHistory : {}", id);
        searchHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("searchHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/searchHistorys/:query -> search for the searchHistory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/searchHistorys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SearchHistory> searchSearchHistorys(@PathVariable String query) {
        log.debug("Request to search SearchHistorys for query {}", query);
        return searchHistoryService.search(query);
    }
}
