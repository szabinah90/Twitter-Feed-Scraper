package posting;

import twitter4j.TwitterException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BulkTweet {
    public void bulkPosting() {
        PostATweet poster = new PostATweet();
        ClassLoader classLoader = getClass().getClassLoader();
        StringBuilder tweets = new StringBuilder();
        BufferedReader reader = null;

        List<String> tweetContainer = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(classLoader.getResource("tweets.txt").getPath()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                tweetContainer.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        for (String tweet : tweetContainer) {
            try {
                poster.posting(tweet);
            } catch (TwitterException te) {
                te.printStackTrace();
            }
        }
    }
}