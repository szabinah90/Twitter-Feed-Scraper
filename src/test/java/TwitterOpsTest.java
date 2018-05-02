import credentials.Credentials;
import credentials.ParseCredentials;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.hamcrest.Matchers;

public class TwitterOpsTest {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream is = classLoader.getResourceAsStream("credentials.json");

    File credentialsFile = new File(classLoader.getResource("credentials.json").getFile());

    @Test
    public void credentialsFileExists() {
        assertTrue(credentialsFile.exists());
    }

    @Test
    public void credentialsFileNotEmpty() {
        assertNotEquals(0, credentialsFile.length());
    }

    @Test
    public void credentialsFormat() {
        ParseCredentials testParsing = new ParseCredentials();
        Credentials testCredentials = testParsing.parsing();

        Pattern consumerKey = Pattern.compile("\\w{25}");
        Pattern consumerSecret = Pattern.compile("\\w{50}");
        Pattern accessToken = Pattern.compile("\\d{18}-\\w{31}");
        Pattern accessTokenSecret = Pattern.compile("\\w{45}");

        assertThat(testCredentials.getConsumerKey(), Matchers.matchesPattern(consumerKey));
        assertThat(testCredentials.getConsumerSecret(), Matchers.matchesPattern(consumerSecret));
        assertThat(testCredentials.getAccessToken(), Matchers.matchesPattern(accessToken));
        assertThat(testCredentials.getAccessTokenSecret(), Matchers.matchesPattern(accessTokenSecret));
    }
}
