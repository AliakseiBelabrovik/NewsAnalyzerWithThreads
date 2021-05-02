package newsreader.downloader;

import newsapi.NewsApiException;
import org.apache.log4j.spi.ThrowableRenderer;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;


public abstract class Downloader {

    public static final String HTML_EXTENTION = ".html";

    /**
     * path to download folder in the project
     */
    public static final String DIRECTORY_DOWNLOAD = "D:\\FH Campus Wien\\2_Semester\\0_Programmieren 2\\" +
            "2_Übungen\\4_Threads\\Code\\src\\main\\java"+File.separator+"download"+File.separator;

    public abstract int process(List<String> urls);



    public String saveUrl2File(String urlString) {
        InputStream is = null;
        OutputStream os = null;
        String fileName = "";
        try {
            URL url4download = new URL(urlString);
            is = url4download.openStream();

            fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            if (fileName.isEmpty()) {
                fileName = url4download.getHost() + HTML_EXTENTION;
            }

            /**
             *if any file's endings are not html -> add html
             */
            if (!fileName.endsWith(".html")){
                fileName = fileName + HTML_EXTENTION;
            }

            //System.out.println("FILE NAME AFTER Substring is " + fileName);
            //System.out.println("with adding download: " + DIRECTORY_DOWNLOAD + fileName);


            os = new FileOutputStream( DIRECTORY_DOWNLOAD + fileName);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            System.out.println("Unable to create a file with a name "
                    +  DIRECTORY_DOWNLOAD + fileName +". Please change or specify the file path.");
        } finally {
            try {
                Objects.requireNonNull(is).close();
                Objects.requireNonNull(os).close();
            } catch (IOException e) {
                System.out.println("Unable to close the streams, " +
                        "possibly because no file was created.");
            } catch (NullPointerException nullPointerException) {
                System.out.println("FileOutputStream is null, because not " +
                        "no file was created. Unable to close it");
            }
        }
        return fileName;
    }
}
