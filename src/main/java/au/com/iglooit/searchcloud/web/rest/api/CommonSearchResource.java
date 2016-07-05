package au.com.iglooit.searchcloud.web.rest.api;

import au.com.iglooit.searchcloud.service.search.IndexDataService;
import au.com.iglooit.searchcloud.web.rest.dto.search.IndexDataResponse;
import com.codahale.metrics.annotation.Timed;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/common/api")
public class CommonSearchResource {
    private final Logger log = LoggerFactory.getLogger(CommonSearchResource.class);

    @Inject
    private IndexDataService indexDataService;

    @RequestMapping(value = "/data/{type}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IndexDataResponse> createIndexData(@PathVariable String type,
                                                             @RequestBody String jsonString) throws
            URISyntaxException {
        log.debug("REST request to update index data: type is {}; data is {}", type, jsonString);
        return new ResponseEntity<>(new IndexDataResponse(indexDataService.createIndexData(type, jsonString)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/data/{type}/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IndexDataResponse> createIndexData(@PathVariable String id, @PathVariable String type,
                                                             @RequestBody String jsonString) throws
            URISyntaxException {
        log.debug("REST request to update index data: type is {}; data is {}", type, jsonString);
        return new ResponseEntity<>(new IndexDataResponse(indexDataService.updateIndexData(id, type, jsonString)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/data/_mapping/{type}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IndexDataResponse> updateMapping(@PathVariable String type,
                                                           @RequestBody String jsonString) throws
            URISyntaxException {
        log.debug("REST request to update index mapping: type is {}; map is {}", type, jsonString);
        Boolean result = indexDataService.updateIndexMap(type, jsonString);
        if (result) {
            return new ResponseEntity<>(new IndexDataResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new IndexDataResponse(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/data/_search/{type}/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SearchResponse> searchMembersInCompany(@PathVariable String type,
                                                                 @PathVariable String queryString) {
        log.debug("Request to search Members for query {}", queryString);
        return new ResponseEntity<SearchResponse>(indexDataService.search(type, queryString), HttpStatus.OK);
    }

}
