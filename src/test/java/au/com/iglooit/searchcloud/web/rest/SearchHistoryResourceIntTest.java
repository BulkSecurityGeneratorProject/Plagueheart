package au.com.iglooit.searchcloud.web.rest;

import au.com.iglooit.searchcloud.Application;
import au.com.iglooit.searchcloud.domain.SearchHistory;
import au.com.iglooit.searchcloud.repository.SearchHistoryRepository;
import au.com.iglooit.searchcloud.service.SearchHistoryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SearchHistoryResource REST controller.
 *
 * @see SearchHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SearchHistoryResourceIntTest {

    private static final String DEFAULT_QUERY = "AAAAA";
    private static final String UPDATED_QUERY = "BBBBB";

    private static final LocalDate DEFAULT_QUERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_QUERY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SearchHistoryRepository searchHistoryRepository;

    @Inject
    private SearchHistoryService searchHistoryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSearchHistoryMockMvc;

    private SearchHistory searchHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SearchHistoryResource searchHistoryResource = new SearchHistoryResource();
        ReflectionTestUtils.setField(searchHistoryResource, "searchHistoryService", searchHistoryService);
        this.restSearchHistoryMockMvc = MockMvcBuilders.standaloneSetup(searchHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        searchHistory = new SearchHistory();
        searchHistory.setQuery(DEFAULT_QUERY);
        searchHistory.setQueryDate(DEFAULT_QUERY_DATE);
    }

    @Test
    @Transactional
    public void createSearchHistory() throws Exception {
        int databaseSizeBeforeCreate = searchHistoryRepository.findAll().size();

        // Create the SearchHistory

        restSearchHistoryMockMvc.perform(post("/api/searchHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchHistory)))
                .andExpect(status().isCreated());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistorys = searchHistoryRepository.findAll();
        assertThat(searchHistorys).hasSize(databaseSizeBeforeCreate + 1);
        SearchHistory testSearchHistory = searchHistorys.get(searchHistorys.size() - 1);
        assertThat(testSearchHistory.getQuery()).isEqualTo(DEFAULT_QUERY);
        assertThat(testSearchHistory.getQueryDate()).isEqualTo(DEFAULT_QUERY_DATE);
    }

    @Test
    @Transactional
    public void getAllSearchHistorys() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        // Get all the searchHistorys
        restSearchHistoryMockMvc.perform(get("/api/searchHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(searchHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())))
                .andExpect(jsonPath("$.[*].queryDate").value(hasItem(DEFAULT_QUERY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

        // Get the searchHistory
        restSearchHistoryMockMvc.perform(get("/api/searchHistorys/{id}", searchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(searchHistory.getId().intValue()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY.toString()))
            .andExpect(jsonPath("$.queryDate").value(DEFAULT_QUERY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearchHistory() throws Exception {
        // Get the searchHistory
        restSearchHistoryMockMvc.perform(get("/api/searchHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

		int databaseSizeBeforeUpdate = searchHistoryRepository.findAll().size();

        // Update the searchHistory
        searchHistory.setQuery(UPDATED_QUERY);
        searchHistory.setQueryDate(UPDATED_QUERY_DATE);

        restSearchHistoryMockMvc.perform(put("/api/searchHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(searchHistory)))
                .andExpect(status().isOk());

        // Validate the SearchHistory in the database
        List<SearchHistory> searchHistorys = searchHistoryRepository.findAll();
        assertThat(searchHistorys).hasSize(databaseSizeBeforeUpdate);
        SearchHistory testSearchHistory = searchHistorys.get(searchHistorys.size() - 1);
        assertThat(testSearchHistory.getQuery()).isEqualTo(UPDATED_QUERY);
        assertThat(testSearchHistory.getQueryDate()).isEqualTo(UPDATED_QUERY_DATE);
    }

    @Test
    @Transactional
    public void deleteSearchHistory() throws Exception {
        // Initialize the database
        searchHistoryRepository.saveAndFlush(searchHistory);

		int databaseSizeBeforeDelete = searchHistoryRepository.findAll().size();

        // Get the searchHistory
        restSearchHistoryMockMvc.perform(delete("/api/searchHistorys/{id}", searchHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SearchHistory> searchHistorys = searchHistoryRepository.findAll();
        assertThat(searchHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
