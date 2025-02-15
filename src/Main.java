import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Main {

    public static void main(String[] args) {

        String url = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_expressions";
        String wordRegex = "(?i)\\b(regular\\w*)";
        int maxPages = 15;
        boolean multithreaded = false;

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("crawling").withCodecRegistry(pojoCodecRegistry);
            CrawlingDataDao dao = new CrawlingDataDao(db);
            Crawler crawler = getImplementation(multithreaded, url, wordRegex, maxPages, dao);

            crawler.crawl();
        }

    }

    private static Crawler getImplementation(boolean multithreaded, String url, String wordRegex, int maxPages,
                                             CrawlingDataDao dao) {
        if (multithreaded) {
            return new CrawlerMultiThreaded(url, wordRegex, maxPages, dao);
        }

        return new CrawlerSingleThreaded(url, wordRegex, maxPages, dao);
    }
}