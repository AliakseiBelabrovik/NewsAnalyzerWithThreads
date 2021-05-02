package newsreader.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelDownloader extends Downloader {



    @Override
    public int process(List<String> urls) throws DownloadException {

        List<Task> taskList = new ArrayList<>();
        for (String url : urls) {
            taskList.add(new Task(this, url));
        }
        //Eine wichtige statische Methode der Klasse Executors ist newCachedThreadPool().
        // Das Ergebnis ist ein ExecutorService-Objekt,
        // eine Implementierung von Executor mit der Methode execute(Runnable):
        //Liefert einen Thread-Pool mit wachsender Größe.

        List<Future<String>> stringFuture = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            stringFuture = executorService.invokeAll(taskList);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            throw new DownloadException("Exception while invoking all threads for downloading articles.");
        }
        executorService.shutdown();

        //assert stringFuture != null;
        List<String> fileNames = new ArrayList<>();
        //System.out.println("Size of filesNames is " + fileNames.size());
        int count = 0;
        //System.out.println("String Future size " + stringFuture.size());


        for (int i = 0; i < stringFuture.size(); i++) {
            try {
                fileNames.add(stringFuture.get(i).get()); //firstly, gets the future object at index and then gets
                //the fileNames of callable which is a string
                count++;
            } catch (InterruptedException e) {
                throw new DownloadException("Unable to finish the thread. The thread was interrupted.");
            } catch (ExecutionException e) {
                throw new DownloadException("Exception while downloading the article.");
            }


        }

        return count;
    }

}
