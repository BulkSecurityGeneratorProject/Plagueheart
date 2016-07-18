package au.com.iglooit.searchcloud.web.rest.dto;

import au.com.iglooit.searchcloud.cons.DocumentAPIConstants;
import au.com.iglooit.searchcloud.domain.api.PDocument;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Created by nicholaszhu on 16/07/2016.
 */
public class PDocumentDTO implements Serializable {

    private Integer companyId;

    private String apiKey;

    private Integer docId;

    private String title;

    private String fileName;

    private String fileSize;

    private String createdBy;

    private LocalDate createdDateTime;

    private String appMeta;

    private List<Integer> tags;

    private String downloadURL;

    public PDocumentDTO() {

    }

    public PDocumentDTO(PDocument pDocument) {
        companyId = pDocument.getCompanyId();
        apiKey = pDocument.getApiKey();
        docId = pDocument.getDocId();
        title = pDocument.getTitle();
        fileName = pDocument.getFileName();
        fileSize = pDocument.getFileSize();
        createdBy = pDocument.getCreatedBy();
        createdDateTime = pDocument.getCreatedDateTime();
        appMeta = pDocument.getAppMeta();
        tags = pDocument.getTags().stream().map(d -> d).collect(toList());
        downloadURL = DocumentAPIConstants.DOCUMENT_API_DOWNLOAD_URL + pDocument.getId();
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

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    @Override
    public String toString() {
        return "PDocument{" +
                ", companyId='" + companyId + "'" +
                ", apiKey='" + apiKey + "'" +
                ", docId='" + docId + "'" +
                ", title='" + title + "'" +
                ", fileName='" + fileName + "'" +
                ", fileSize='" + fileSize + "'" +
                ", createdBy='" + createdBy + "'" +
                ", createdDateTime='" + createdDateTime + "'" +
                ", downloadURL='" + downloadURL + "'" +
                ", tags={" + tags + "}" +
                ", appMeta='" + appMeta + "'" +
                '}';
    }

}
