package elasticConnector;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import readTextFiles.ReadTextFiles;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.io.IOException;
import java.util.HashMap;

public class MyElasticConnector {

    private final String PATH = "./data/elasticMap.json";

    public MyElasticConnector() {
    }

    public void createIndex(String indexName) {
        ReadTextFiles fileReader = new ReadTextFiles();

        RestHighLevelClient client = null;
        CreateIndexRequest request = null;
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        request = new CreateIndexRequest(indexName);
        String mappingFile = fileReader.readFromFile(PATH);
        request.mapping("tweet", mappingFile, XContentType.JSON);

        try {
            client.indices().create(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Index created.");
    }

    public void uploadingDocuments(Status status, String indexName) {
        RestHighLevelClient client = null;
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        IndexRequest request = new IndexRequest(indexName, "tweet");

        HashMap<String, Object> outerMap = new HashMap<>();
        outerMap.put("created_at", status.getCreatedAt());
        outerMap.put("tweet_id", String.valueOf(status.getId()));
        outerMap.put("text", status.getText());
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
