import org.jsoup.HttpStatusException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerSingleThreaded {

    private final String startingUrl;
    private final String regex;
    private final int maxPages;
    private final CrawlingDataDao dao;
    private final Pattern regexPattern;

    public CrawlerSingleThreaded(String startingUrl, String regex, int maxPages, CrawlingDataDao dao) {
        this.startingUrl = startingUrl;
        this.regex = regex;
        this.maxPages = maxPages;
        this.dao = dao;

        regexPattern = Pattern.compile(regex);
    }

    public void crawl() {
        WebPageFetcher webPageFetcher = new WebPageFetcher();

        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.add(startingUrl);
        visited.add(startingUrl);
        int visitedPages = 0;

        while (!stack.isEmpty() && visitedPages < maxPages) {
            String urlToCrawl = stack.pop();

            System.out.println("Going to crawl " + urlToCrawl);

            if (!Validator.isValidUrl(urlToCrawl)) {
                System.out.println("Invalid URL: " + urlToCrawl);
                continue;
            }

            String content;
            try {
                content = webPageFetcher.getWebPage(urlToCrawl);
            } catch (IOException ex) {
                if (ex instanceof HttpStatusException && ((HttpStatusException) ex).getStatusCode() == 404) {
                    System.out.println("Given url " + urlToCrawl + "not found!");
                }

                System.out.println("Exception getting web page " + urlToCrawl + " : " + ex.getMessage());
                continue;
            }

            Set<String> matches = findMatchingText(content);

            CrawlingData crawlingData = new CrawlingData(urlToCrawl, regex, matches);
            dao.save(crawlingData);

            Matcher urlMatcher = UrlMatcherFactory.getPattern(true).matcher(content);

            while (urlMatcher.find()) {
                String newUrl = urlMatcher.group(1);
                boolean success = visited.add(newUrl);
                if (success) {
                    stack.push(newUrl);
                    System.out.println("Found new URL: " + newUrl);
                }
            }

            visitedPages++;
        }
    }

    private Set<String> findMatchingText(String content) {
        Matcher matcher = regexPattern.matcher(content);

        Set<String> matches = new HashSet<>();

        while (matcher.find()) {
            String result = matcher.group();
            boolean success = matches.add(result);
            if (success) {
                System.out.println("Found new result: " + result);

            }
        }

        return matches;
    }

}
