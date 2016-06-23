package au.com.iglooit.searchcloud.repository;

import au.com.iglooit.searchcloud.domain.SearchHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SearchHistory entity.
 */
public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {

}
