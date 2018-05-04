package stream;

import com.google.api.client.util.Preconditions;
import credentials.Credentials;
import credentials.MyConfigurationBuilder;
import credentials.ParseCredentials;
import googledrive.DownloadPictures;
import googledrive.GoogleDrive;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class FilteredStream {

    public void filtering(String filter) {
        MyConfigurationBuilder builder = new MyConfigurationBuilder();
        ConfigurationBuilder cb = builder.buildConfig();

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        twitterStream.addListener(new StatusListener() {
            int counter = 1;
            int picCounter = 1;

            GoogleDrive googleDrive = new GoogleDrive();

            @Override
            public void onStatus(Status status) {
                DownloadPictures downloadPictures = new DownloadPictures();
                MediaEntity[] mediaEntities = status.getMediaEntities();
                for (MediaEntity mediaEntity : mediaEntities) {
                    if (mediaEntity.getType().equals("photo")) {
                        downloadPictures.download(mediaEntity.getMediaURL());
                        googleDrive.executeUpload();
                        picCounter++;
                    }
                }
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
        tweetFilterQuery.language("en");
        twitterStream.filter(tweetFilterQuery);
    }
}
