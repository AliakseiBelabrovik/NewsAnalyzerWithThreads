package newsreader.downloader;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;


public abstract class Downloader {

    public static final String HTML_EXTENTION = ".html";

    /**
     * path to download folder in the project
     */
    public static final String DIRECTORY_DOWNLOAD = "." + File.separator + "download" + File.separator;

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

            fileName = fileName.replaceAll(",","");
            fileName = fileName.replaceFirst("\\?", "");

            /**
             *if any file's endings are not html -> add html
             */
            if (!fileName.endsWith(".html")){
                fileName = fileName + HTML_EXTENTION;
            }

            os = new FileOutputStream( DIRECTORY_DOWNLOAD + fileName);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            System.out.println("Unable to create a file with a name "
                    +  DIRECTORY_DOWNLOAD + fileName +". Please change or specify the file path/filename.");
            return null;
        } finally {
            try {
                Objects.requireNonNull(is).close();
                Objects.requireNonNull(os).close();
            } catch (IOException e) {
                System.out.println("Unable to close the streams, possibly because no file was created.");
            } catch (NullPointerException nullPointerException) {
                System.out.println("FileOutputStream is null, because no file was created. Unable to close it");
            }
        }
        return fileName;
    }
}
