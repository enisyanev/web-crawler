import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class CrawlingDataDao {

    private final MongoCollection<Document> urls;

    public CrawlingDataDao(MongoDatabase mongoDatabase) {
        urls = mongoDatabase.getCollection("urls");
    }

    // Insert the CrawlingData in the DB
    public void save(CrawlingData crawlingData) {
        urls.insertOne(new Document(crawlingData.getUrl(), crawlingData));
    }

}
