package feed;

import credentials.MyConfigurationBuilder;
import elasticConnector.MyElasticConnector;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetFeed {
    private String username;
    private int numberOfTweets;
    private boolean isAll;
    private String indexName;

    /**
     * @param isAll: if you want the max number of twets (about 3200) from a user's timeline, set this parameter to true,
     *             and use the first constructor. Otherwise, set it to false and give the @numberOfTweets parameter (use the second constructor).
     * @param username: desired username
     * @param indexName: index name you wish to use on ElasticSearch
     */
    public GetFeed(boolean isAll, String username, String indexName) {
        this.username = username;
        this.numberOfTweets = 0;
        this.isAll = isAll;
        this.indexName = indexName;
    }

    public GetFeed(boolean isAll, int numberOfTweets, String username, String indexName) {
        this.numberOfTweets = numberOfTweets;
        this.username = username;
        this.numberOfTweets = numberOfTweets;
        this.isAll = isAll;
        this.indexName = indexName;
    }



    public void gettingFeed() {
        MyConfigurationBuilder builder = new MyConfigurationBuilder();
        ConfigurationBuilder cb = builder.buildConfig();

        MyElasticConnector elasticConnector = new MyElasticConnector();

        elasticConnector.createIndex(indexName);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter1 = tf.getInstance();

        List<Status> statuses = new ArrayList<>();

        int pageno = 1;

        try {
            if (isAll) {
                while (true) {
                    int size = statuses.size();
                    Paging page = new Paging(pageno++, 50);
                    statuses.addAll(twitter1.getUserTimeline(username, page));
                    if (statuses.size() == size)
                        break;
                }
            } else {
                while (true) {
                    int size = statuses.size();
                    Paging page = new Paging(pageno++, numberOfTweets);
                    statuses.addAll(twitter1.getUserTimeline(username, page));
                    if (statuses.size() == numberOfTweets)
                        break;
                }
            }

            System.out.println("Showing @" + username + "'s user timeline.");
            int counter = 1;
            for (Status status : statuses) {
                System.out.println("[" + counter + "] " + status.getCreatedAt() + " @" + status.getUser().getScreenName() + " - " + status.getText());
                counter++;
                elasticConnector.uploadingDocuments(status, indexName);
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
}
