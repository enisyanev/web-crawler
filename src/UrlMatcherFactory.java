import java.util.regex.Pattern;

public class UrlMatcherFactory {

    // ако се търси само в текста на html-a, a не в href таговете
    private static final String URL_REGEX = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private static final String HREF_URL_REGEX = "<a[^<]*href=\"(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*))\"";
    private static final Pattern HREF_URL_PATTERN = Pattern.compile(HREF_URL_REGEX);


    public static Pattern getPattern(boolean hrefOnly) {
        if (hrefOnly) {
            return HREF_URL_PATTERN;
        }

        return URL_PATTERN;
    }
}
