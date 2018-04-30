package readTextFiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadTextFiles {

    public List<String> readFromFile(String filename) {

        BufferedReader reader = null;
        List<String> spamWords = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(new java.io.File(filename))); // if you have an existing file, "new java.io" is omitted
            String line = null;

            while ((line = reader.readLine()) != null) {
                spamWords.add(line);
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
        return spamWords;
    }

    public boolean stringContainsItemFromList(String inputStr, List<String> items) {
        return items.parallelStream().anyMatch(inputStr::contains);
    }
}
