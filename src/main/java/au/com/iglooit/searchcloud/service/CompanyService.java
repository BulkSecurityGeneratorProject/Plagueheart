package au.com.iglooit.searchcloud.service;

import au.com.iglooit.searchcloud.domain.Company;
import au.com.iglooit.searchcloud.repository.CompanyRepository;
import au.com.iglooit.searchcloud.repository.search.CompanySearchRepository;
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
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    
    @Inject
    private CompanyRepository companyRepository;
    
    @Inject
    private CompanySearchRepository companySearchRepository;
    
    /**
     * Save a company.
     * @return the persisted entity
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        Company result = companyRepository.save(company);
        companySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the companys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companys");
        Page<Company> result = companyRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one company by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Company findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOne(id);
        return company;
    }

    /**
     *  delete the  company by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
        companySearchRepository.delete(id);
    }

    /**
     * search for the company corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Company> search(String query) {
        
        log.debug("REST request to search Companys for query {}", query);
        return StreamSupport
            .stream(companySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
