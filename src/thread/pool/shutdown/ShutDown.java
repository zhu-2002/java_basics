package thread.pool.shutdown;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 关闭线程池
 */
public class ShutDown {
    public static void main(String[] args) throws InterruptedException {
        int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < 1000 ; i++) {
            executorService.execute(new ShutDownTask());
        }
        Thread.sleep(1500);
//        executorService.shutdown();
        // 返回值是在队列中但还未执行的线程
        List<Runnable> runnableList = executorService.shutdownNow();
        System.out.println(runnableList);
    }
}


