package au.com.iglooit.searchcloud.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nicholaszhu on 16/07/2016.
 */
public final class PDocumentUtil {
    private PDocumentUtil() {

    }

    public static List<Integer> parseTagID(String tags) {
        String[] rawList = tags.split(",");
        if(rawList == null) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(rawList).stream().map(keyStr -> {
                return Integer.valueOf(keyStr);
            }).collect(Collectors.toList());
        }
    }

    /**
     *
     * @param localDate 2016-07-14
     * @return
     */
    public static LocalDate parseLocalDate(String localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(localDate, formatter);
    }
}
