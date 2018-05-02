package googledrive;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadPictures {
    public void download(String url){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new URL(url).openStream();
            outputStream = new FileOutputStream(new File("image.jpg"));
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
