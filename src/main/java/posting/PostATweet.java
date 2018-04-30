package posting;

import credentials.Credentials;
import credentials.ParseCredentials;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PostATweet {
    public void posting(String tweetText) throws TwitterException {
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
        twitter.updateStatus(tweetText);
        System.out.println("Tweet posted successfully.");
    }
}
