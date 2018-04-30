package stream;

import org.codehaus.jackson.map.ObjectMapper;
import posting.Tweet;
import twitter4j.Status;
import users.User;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONParser {

    public void parseStatus(Status status) {
        ClassLoader classLoader = getClass().getClassLoader();
        User user = new User(String.valueOf(status.getUser().getId()), status.getUser().getName(), status.getUser().getScreenName(), status.getUser().getLocation(), status.getUser().getURL(), status.getUser().getDescription());
        Tweet tweet = new Tweet(status.getCreatedAt().toString(), String.valueOf(status.getId()), status.getText(), user);


    }
}
