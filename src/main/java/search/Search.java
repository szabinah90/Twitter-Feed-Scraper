package search;

import credentials.Credentials;
import credentials.ParseCredentials;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class Search {
    public void searchKeyword(String keyword) {
        ParseCredentials parser = new ParseCredentials();
        Credentials credentials = parser.parsing();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(credentials.getConsumerKey())
                .setOAuthConsumerSecret(credentials.getConsumerSecret())
                .setOAuthAccessToken(credentials.getAccessToken())
                .setOAuthAccessTokenSecret(credentials.getAccessTokenSecret())
                .setTweetModeExtended(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        int counter = 1;

        try {
            Query query = new Query(keyword);
            // query.count(100);
            QueryResult result = null;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println( "[" + counter + "] " + tweet.getCreatedAt() + ": @" + tweet.getUser().getScreenName() + ": " + tweet.getText());
                    counter++;
                }
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }
}
