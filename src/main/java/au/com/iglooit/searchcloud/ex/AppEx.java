package au.com.iglooit.searchcloud.ex;

/**
 * Created by nicholaszhu on 16/07/2016.
 */
public class AppEx extends RuntimeException {
    public AppEx() {
    }

    public AppEx(String message) {
        super(message);
    }

    public AppEx(String message, Throwable cause) {
        super(message, cause);
    }
}
