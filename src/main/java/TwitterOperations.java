import feed.GetFeed;
import posting.BulkTweet;
import posting.PostATweet;
import search.Search;
import stream.SampleStream;
import twitter4j.TwitterException;

public class TwitterOperations {

    public static void main(String[] args) throws TwitterException {
        /**
         * Get the last 1000 tweet from a user's timeline and upload to ElasticSearch
         */
        GetFeed userTimeline = new GetFeed(false, 1000, "username", "ES_index_name", "ES_type");
        userTimeline.gettingFeed();

        /**
         * Get the public live feed stream and upload the data extracted to ElasticSearch
         */
        SampleStream sampleStream = new SampleStream("ES_index_name", "ES_Type", "language_code");
        sampleStream.samplingStream("en");

        /**
         * Search for specific keywords (no ES upload included)
         */
        Search keyword = new Search();
        keyword.searchKeyword("health");

        /**
         * Posting individual tweets and bulk from a file
         */
        PostATweet sampleTweet = new PostATweet();
        sampleTweet.posting("Automated tweet from JAVA app");

        BulkTweet sampleBulk = new BulkTweet();
        sampleBulk.bulkPosting();
    }
}