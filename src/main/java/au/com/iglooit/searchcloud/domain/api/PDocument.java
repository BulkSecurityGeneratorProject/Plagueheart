package au.com.iglooit.searchcloud.domain.api;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "searchcloud", type = "document")
public class PDocument implements Serializable {

    private Long id;

    private Integer companyId;

    private String apiKey;

    private Integer docId;

    private String title;

    private String fileName;

    private String fileSize;

    private String createdBy;

    private LocalDate createdDateTime;

    private String appMeta;

    private String file;

    private List<Integer> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDate createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getAppMeta() {
        return appMeta;
    }

    public void setAppMeta(String appMeta) {
        this.appMeta = appMeta;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PDocument pDocument = (PDocument) o;
        if(pDocument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PDocument{" +
                "id=" + id +
                ", companyId='" + companyId + "'" +
                ", apiKey='" + apiKey + "'" +
                ", docId='" + docId + "'" +
                ", title='" + title + "'" +
                ", fileName='" + fileName + "'" +
                ", fileSize='" + fileSize + "'" +
                ", createdBy='" + createdBy + "'" +
                ", createdDateTime='" + createdDateTime + "'" +
                ", tags={" + tags + "}" +
                ", appMeta='" + appMeta + "'" +
                '}';
    }
}
