package au.com.iglooit.searchcloud.web.rest;

import au.com.iglooit.searchcloud.cons.DocumentAPIConstants;
import au.com.iglooit.searchcloud.domain.api.PDocument;
import au.com.iglooit.searchcloud.ex.AppEx;
import au.com.iglooit.searchcloud.service.api.PDocumentSearchService;
import au.com.iglooit.searchcloud.service.util.AesCryptUtil;
import au.com.iglooit.searchcloud.util.PDocumentUtil;
import au.com.iglooit.searchcloud.web.rest.dto.PDocumentDTO;
import au.com.iglooit.searchcloud.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by nicholaszhu on 15/07/2016.
 */
@CrossOrigin(maxAge = 6000)
@RestController
@RequestMapping(DocumentAPIConstants.DOCUMENT_API_BASE)
public class DocumentAPIResource {

    private final Logger log = LoggerFactory.getLogger(DocumentAPIResource.class);

    @Inject
    private PDocumentSearchService pDocumentSearchService;

    @RequestMapping(value = "/_upload", headers = "content-type=multipart/*", method = RequestMethod.POST)
    @Timed
    public
    @ResponseBody
    ResponseEntity<PDocumentDTO> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("document") PDocument pDocument) {
        if (!file.isEmpty()) {
            try {
                String content = DatatypeConverter.printBase64Binary(file.getBytes());
                pDocument.setFile(content);
                pDocumentSearchService.saveDocumentToCloud(pDocument);
                return new ResponseEntity<PDocumentDTO>(pDocumentSearchService.saveDocumentToCloud(pDocument),
                        HttpStatus.OK);
            } catch (Exception e) {
                log.error("Can not upload document: ", pDocument, e);
                return new ResponseEntity<PDocumentDTO>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<PDocumentDTO>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/_upload/document", headers = "content-type=multipart/*", method = RequestMethod.POST)
    @Timed
    public
    @ResponseBody
    ResponseEntity<PDocumentDTO> handleCreateDocument(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("companyId") Integer companyId,
                                                      @RequestParam("apiKey") String apiKey,
                                                      @RequestParam("docId") Integer docId,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("tags") String tags,
                                                      @RequestParam("fileName") String fileName,
                                                      @RequestParam("fileSize") String fileSize,
                                                      @RequestParam("createdBy") String createdBy,
                                                      @RequestParam("createdDateTime") String createdDateTime,
                                                      @RequestParam("appMeta") String appMeta) {
        if (!file.isEmpty()) {
            try {
                String content = DatatypeConverter.printBase64Binary(file.getBytes());
                PDocument pDocument = new PDocument();
                pDocument.setId(Long.valueOf(docId));
                pDocument.setCompanyId(companyId);
                pDocument.setApiKey(apiKey);
                pDocument.setDocId(docId);
                pDocument.setTitle(title);
                pDocument.setTags(PDocumentUtil.parseTagID(tags));
                pDocument.setFileName(fileName);
                pDocument.setFileSize(fileSize);
                pDocument.setCreatedBy(createdBy);
                pDocument.setCreatedDateTime(PDocumentUtil.parseLocalDate(createdDateTime));
                pDocument.setAppMeta(appMeta);
                pDocument.setFile(content);
                pDocumentSearchService.saveDocumentToCloud(pDocument);
                return new ResponseEntity<PDocumentDTO>(pDocumentSearchService.saveDocumentToCloud(pDocument),
                        HttpStatus.OK);
            } catch (Exception e) {
                log.error("Can not upload document, id is: ", docId, e);
                return new ResponseEntity<PDocumentDTO>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<PDocumentDTO>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/_search/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PDocumentDTO>> searchDocument(@PathVariable String query) {
        log.debug("Request to search document for query {}", query);
        return new ResponseEntity<List<PDocumentDTO>>(pDocumentSearchService.searchByKey(query), HttpStatus.OK);
    }

    @RequestMapping(value = "/{docId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDocument(@PathVariable String docId) {
        log.debug("Request to delete the document for docId {}", docId);
        if (pDocumentSearchService.deleteDocument(docId)) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("docId", docId.toString())).build();
        } else {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert("docId",
                    docId.toString())).build();
        }
    }

    @RequestMapping(value = "/_search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PDocumentDTO>> query(@RequestParam("query") String query) {
        log.debug("Request to search document for query {}", query);
        return new ResponseEntity<List<PDocumentDTO>>(pDocumentSearchService.search(query), HttpStatus.OK);
    }

    @RequestMapping(value = "/_search/company/{companyId}/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PDocumentDTO>> searchDocumentInCompany(@PathVariable("companyId") String companyId,
                                                                      @PathVariable String query) {
        log.debug("Request to search document for query {}", query);
        return new ResponseEntity<List<PDocumentDTO>>(pDocumentSearchService.search(companyId, query), HttpStatus.OK);
    }

    @RequestMapping(value = "/_download/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void downloadDocument(HttpServletResponse response,
                                 @PathVariable("id") String id) {

        log.debug("Request to download document for id {}", id);
        // decode the id firstly
        String esId = AesCryptUtil.decrypt(id, DocumentAPIConstants.DOCUMENT_API_ENCODE_PASSWORD);
        PDocument pDocument = pDocumentSearchService.loadDocument(Long.valueOf(esId));
        String mimeType = URLConnection.guessContentTypeFromName(pDocument.getFileName());
        if (mimeType == null) {
            log.warn("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        log.debug("mimetype : " + mimeType);

        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", String.format("inline; filename=\""
                + pDocument.getFileName() + "\""));

        byte[] rawFile = DatatypeConverter.parseBase64Binary(pDocument.getFile());

        response.setContentLength(rawFile.length);
        try (InputStream inputStream = new ByteArrayInputStream(rawFile)) {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new AppEx("Can not download the file. ", e);
        }
    }
}
