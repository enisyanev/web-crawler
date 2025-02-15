import java.util.Set;

public class CrawlingData {

    private final String url;
    private final String regex;
    private final Set<String> matches;

    public CrawlingData(String url, String regex, Set<String> matches) {
        this.url = url;
        this.regex = regex;
        this.matches = matches;
    }

    public String getUrl() {
        return url;
    }

    public String getRegex() {
        return regex;
    }

    public Set<String> getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        return "URL: " + url + ", REGEX: " + regex + ", MATCHES: " + matches;
    }
}
