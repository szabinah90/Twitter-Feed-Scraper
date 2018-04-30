package feed;

import credentials.Credentials;
import credentials.ParseCredentials;
import elasticConnector.MyElasticConnector;
import readTextFiles.ReadTextFiles;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;
import java.util.List;

public class GetFeed {
    private String username;
    private int numberOfTweets;
    private boolean isAll;
    private String indexName;
    private String type;

    /**
     * @param isAll: if you want the max number of twets (about 3200) from a user's timeline, set this parameter to true,
     *             and use the first constructor. Otherwise, set it to false and give the @numberOfTweets parameter (use the second constructor).
     * @param username: desired username
     * @param indexName: index name you wish to use on ElasticSearch
     * @param type: doc type you wish to use on ElasticSearch
     */
    public GetFeed(boolean isAll, String username, String indexName, String type) {
        this.username = username;
        this.numberOfTweets = 0;
        this.isAll = isAll;
        this.indexName = indexName;
        this.type = type;
    }

    public GetFeed(boolean isAll, int numberOfTweets, String username, String indexName, String type) {
        this.numberOfTweets = numberOfTweets;
        this.username = username;
        this.numberOfTweets = numberOfTweets;
        this.isAll = isAll;
        this.indexName = indexName;
        this.type = type;
    }



    public void gettingFeed() {
        ParseCredentials parser = new ParseCredentials();
        Credentials credentials = parser.parsing();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(credentials.getConsumerKey())
                .setOAuthConsumerSecret(credentials.getConsumerSecret())
                .setOAuthAccessToken(credentials.getAccessToken())
                .setOAuthAccessTokenSecret(credentials.getAccessTokenSecret())
                .setTweetModeExtended(true);

        MyElasticConnector elasticConnector = new MyElasticConnector();

        elasticConnector.createIndex(indexName, type);

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
                elasticConnector.uploadingDocuments(status, indexName, type);
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
}
