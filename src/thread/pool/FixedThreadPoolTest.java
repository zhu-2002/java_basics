package thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// 自动创建线程池
public class FixedThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4) ;
        for (int i = 0; i < 1000 ; i++) {
            executorService.execute(new Task());
        }
    }
}


