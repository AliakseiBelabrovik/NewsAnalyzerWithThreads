package newsreader.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelDownloader extends Downloader {


    /**
     * Process method creates a list of tasks (each article to be downloaded is treated as a separate task)
     * Then, an ExecutorService is created to start a ThreadPool and invoke oll the task on the same time
     * @param urls - list of URLs
     * @return - int coung - number of articles that have been downloaded
     * @throws DownloadException - throws Exception if processing of any threads was unsuccessful
     */
    @Override
    public int process(List<String> urls) {

        List<Task> taskList = new ArrayList<>();
        for (String url : urls) {
            taskList.add(new Task(this, url));
        }
        List<Future<String>> stringFuture = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            stringFuture = executorService.invokeAll(taskList);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Exception while invoking all threads for downloading articles: " + e.getMessage());
        }
        executorService.shutdown();

        List<String> fileNames = new ArrayList<>();
        int count = 0;;
        for (Future<String> future : stringFuture) {
            try {
                fileNames.add(future.get()); //firstly, gets the future object at index and then gets
                //the fileNames of callable which is a string
                //Wartet auf das Ergebnis und gibt es dann zurück. Die Methode blockiert so lange, bis das Ergebnis da ist.
                count++;
            } catch (InterruptedException e) {
                System.out.println("Unable to finish the thread. The thread was interrupted: " + e.getMessage());
            } catch (ExecutionException e) {
                System.out.println("Exception while downloading the article: " + e.getMessage());
            }


        }

        return count;
    }

}
