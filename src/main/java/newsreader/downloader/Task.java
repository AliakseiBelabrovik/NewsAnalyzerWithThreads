package newsreader.downloader;

import java.util.concurrent.Callable;

public class Task implements Callable<String> {

    private final String url;
    private ParallelDownloader parallelDownloader;

    public Task(ParallelDownloader parallelDownloader, String url) {
        this.url = url;
        this.parallelDownloader = parallelDownloader;
    }

    @Override
    public String call() throws DownloadException, Exception {
        return parallelDownloader.saveUrl2File(this.url);
    }
}
