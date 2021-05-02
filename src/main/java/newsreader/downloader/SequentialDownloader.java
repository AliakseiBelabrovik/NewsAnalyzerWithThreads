package newsreader.downloader;

import java.util.List;
import java.util.Objects;

public class SequentialDownloader extends Downloader {

    @Override
    public int process(List<String> urls) {
        int count = 0;
        for (String url : urls) {
            String fileName = saveUrl2File(url);
            if(fileName != null)
                count++;
        }
        return count;
    }
}
