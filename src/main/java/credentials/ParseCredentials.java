package credentials;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParseCredentials {

    public Credentials parsing() {
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] db = new byte[0];
        try {
            db = Files.readAllBytes(Paths.get(classLoader.getResource("credentials.json").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Credentials credentials = null;
        try {
            credentials = objectMapper.readValue(db, Credentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

}
