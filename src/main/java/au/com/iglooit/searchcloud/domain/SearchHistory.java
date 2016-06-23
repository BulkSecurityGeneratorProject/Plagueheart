package au.com.iglooit.searchcloud.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SearchHistory.
 */
@Entity
@Table(name = "search_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "searchhistory")
public class SearchHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "query")
    private String query;
    
    @Column(name = "query_date")
    private LocalDate queryDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }

    public LocalDate getQueryDate() {
        return queryDate;
    }
    
    public void setQueryDate(LocalDate queryDate) {
        this.queryDate = queryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchHistory searchHistory = (SearchHistory) o;
        if(searchHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, searchHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
            "id=" + id +
            ", query='" + query + "'" +
            ", queryDate='" + queryDate + "'" +
            '}';
    }
}
