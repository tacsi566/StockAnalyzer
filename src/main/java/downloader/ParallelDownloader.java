package downloader;

import stockanalyzer.ui.UserInterface;
import yahooApi.exceptions.YahooException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDownloader extends Downloader{

    @Override
    public int process(List<String> urls) throws YahooException {
        int count = 0;
        // Runtime.getRuntime().availableProcessors() gets the max amount of threads in the JVM
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Collection of returned result of the Callable
        List<Future<String>> fileNames = new ArrayList<>();


        // using submit() we submit a task that has a return value which we don't know yet
        // hence the name Future. The task assigned is the saveJson2File(url)
        for (String url : urls){
            fileNames.add(executorService.submit(()->saveJson2File(url)));
        }


        // Iterate over our "return values" of the Future Objects and count the successfully created file names
        for (Future<String> fileName : fileNames){
            try {
                String filename = fileName.get();
                if (filename != null){
                    count++;
                }
            } catch (InterruptedException | ExecutionException e) {
                //throw new YahooException("Something went wrong while multithreading.");
                UserInterface.print(e.toString());
            }

        }

        executorService.shutdown();

        return count;


    }
}
