package au.com.iglooit.searchcloud.web.rest.dto.search;

/**
 * Created by nicholaszhu on 29/06/2016.
 */
public class IndexDataResponse {
    private String status;
    private String errorMessage;
    private String data;

    public IndexDataResponse() {

    }

    public IndexDataResponse(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
