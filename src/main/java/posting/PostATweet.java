package posting;

import credentials.MyConfigurationBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PostATweet {
    public void posting(String tweetText) throws TwitterException {
        MyConfigurationBuilder builder = new MyConfigurationBuilder();
        ConfigurationBuilder cb = builder.buildConfig();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        twitter.updateStatus(tweetText);
        System.out.println("Tweet posted successfully.");
    }
}
