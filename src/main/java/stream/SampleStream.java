package stream;

import credentials.Credentials;
import credentials.ParseCredentials;
import elasticConnector.MyElasticConnector;
import readTextFiles.ReadTextFiles;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class SampleStream {
    private String indexName;
    private String type;
    private String languageCode;

    public SampleStream(String indexName, String type, String languageCode) {
        this.indexName = indexName;
        this.type = type;
        this.languageCode = languageCode;
    }

    public void samplingStream(String languageCode) {
        ReadTextFiles reader = new ReadTextFiles();
        List<String> spam = reader.readFromFile("./data/spam.txt");
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
        MyElasticConnector elasticConnector = new MyElasticConnector();

        elasticConnector.createIndex(indexName, type);

        twitterStream.addListener(new StatusListener() {
            int counter = 1;
            @Override
            public void onStatus(Status status) {
                if (reader.stringContainsItemFromList(status.getText(), spam)) {
                    System.out.println("SPAM");
                } else {
                    elasticConnector.uploadingDocuments(status, indexName, type);
                    System.out.println("[" + counter + "] " + status.getCreatedAt() + ": @" + status.getUser().getScreenName() + " - " + status.getText() + "location: " + status.getUser().getLocation());
                    counter++;
                }
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

        twitterStream.sample(languageCode);
    }
}
