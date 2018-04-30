package stream;

import credentials.Credentials;
import credentials.ParseCredentials;
import posting.Tweet;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import users.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FilteredStream {

    public void filtering(String filter) {
        ParseCredentials parser = new ParseCredentials();
        Credentials credentials = parser.parsing();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(credentials.getConsumerKey())
                .setOAuthConsumerSecret(credentials.getConsumerSecret())
                .setOAuthAccessToken(credentials.getAccessToken())
                .setOAuthAccessTokenSecret(credentials.getAccessTokenSecret())
                .setTweetModeExtended(true);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        twitterStream.addListener(new StatusListener() {
            int counter = 1;
            @Override
            public void onStatus(Status status) {
                System.out.println("[" + counter + "] " + status.getCreatedAt() + ": @" + status.getUser().getScreenName() + " - " + status.getText());
                counter++;
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(filter);
        twitterStream.filter(tweetFilterQuery);
    }
}
