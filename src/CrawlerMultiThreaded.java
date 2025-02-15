import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerMultiThreaded implements Crawler{

    private static final int THREAD_COUNT = 5;

    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    private final BlockingDeque<String> urlStack = new LinkedBlockingDeque<>();
    private final WebPageFetcher webPageFetcher = new WebPageFetcher();
    private final AtomicInteger pagesCrawled = new AtomicInteger(0);

    private final CrawlingDataDao dao;
    private final String regex;
    private final Pattern regexPattern;
    private final int maxPages;

    public CrawlerMultiThreaded(String startUrl, String regex, int maxPages, CrawlingDataDao dao) {
        this.dao = dao;
        this.regex = regex;
        this.regexPattern = Pattern.compile(regex);
        this.maxPages = maxPages;
        urlStack.push(startUrl);
    }

    @Override
    public void crawl() {
        while (pagesCrawled.get() < maxPages) {
            try {
                String url = urlStack.poll(10, TimeUnit.SECONDS);
                if (url == null) {
                    break;
                }
                if (visitedUrls.add(url)) {
                    executor.submit(() -> crawlInternal(url));
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        shutdown();
    }

    private void crawlInternal(String url) {
        pagesCrawled.incrementAndGet();

        if (pagesCrawled.get() > maxPages) {
            return;
        }

        try {
            System.out.println("Crawling: " + url);
            String html = webPageFetcher.getWebPage(url);
            extractAndStoreMatches(url, html);
            extractUrls(html);
        } catch (Exception e) {
            System.err.println("Failed to crawl " + url + ": " + e.getMessage());
        }
    }

    private void extractAndStoreMatches(String url, String html) {
        Matcher matcher = regexPattern.matcher(html);
        Set<String> urls = new HashSet<>();
        while (matcher.find()) {
            String match = matcher.group();
            urls.add(match);
        }
        CrawlingData crawlingData = new CrawlingData(url, regex, urls);
        dao.save(crawlingData);
    }

    private void extractUrls(String html) {
        Matcher urlMatcher = UrlMatcherFactory.getPattern(true).matcher(html);

        while (urlMatcher.find()) {
            String newUrl = urlMatcher.group(1);
            if (!visitedUrls.contains(newUrl)) {
                urlStack.push(newUrl);
                System.out.println("Found new URL: " + newUrl);
            }
        }
    }

    private void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
