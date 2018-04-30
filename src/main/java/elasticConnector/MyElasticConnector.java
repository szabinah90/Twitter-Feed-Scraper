package elasticConnector;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.io.IOException;
import java.util.HashMap;

public class MyElasticConnector {

    private final String PATH = "./data/elasticMap.json";

    public MyElasticConnector() {
    }

    public void createIndex(String indexName, String type) {
        RestHighLevelClient client = null;
        CreateIndexRequest request = null;
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        request = new CreateIndexRequest(indexName);
        request.mapping(type, PATH, XContentType.JSON);

        System.out.println("Index created.");
    }

    public void uploadingDocuments(Status status, String indexName, String type) {
        RestHighLevelClient client = null;
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        IndexRequest request = new IndexRequest(indexName, type);

        HashMap<String, String> hashtags = new HashMap<>();
        for (HashtagEntity hashtag : status.getHashtagEntities()) {
            hashtags.put("hashtags", hashtag.getText());
        }

        HashMap<String, Object> outerMap = new HashMap<>();
        outerMap.put("created_at", status.getCreatedAt());
        outerMap.put("tweet_id", String.valueOf(status.getId()));
        outerMap.put("text", status.getText());
        outerMap.put("hashtags", hashtags);
        outerMap.put("screen_name", status.getUser().getScreenName());
        outerMap.put("username", status.getUser().getName());
        outerMap.put("location", status.getUser().getLocation());
        try {
            client.index(request.source(outerMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Upload complete.");
    }
}
