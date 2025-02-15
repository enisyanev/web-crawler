import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Main {

    public static void main(String[] args) {

        String url = "http://petpas.100webspace.net/georgi/";
        String wordRegex = "(?i)\\b(regular\\w*)";
        int maxPages = 15;

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("crawling").withCodecRegistry(pojoCodecRegistry);
            CrawlingDataDao dao = new CrawlingDataDao(db);

//            CrawlerSingleThreaded singleThreaded = new CrawlerSingleThreaded(url, wordRegex, maxPages, dao);
//            singleThreaded.crawl();

            CrawlerMultiThreaded multiThreaded = new CrawlerMultiThreaded(url, wordRegex, maxPages, dao);
            multiThreaded.start();
        }





    }
}