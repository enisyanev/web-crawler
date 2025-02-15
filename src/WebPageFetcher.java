import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebPageFetcher {

    public String getWebPage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.html();
    }

}
