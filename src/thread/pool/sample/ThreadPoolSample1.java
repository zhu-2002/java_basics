package thread.pool.sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolSample1 {
    public static void main(String[] args) {
        // 定长线程池，参数是线程个数
        ExecutorService threadPool = Executors.newFixedThreadPool(10) ;
        for (int i = 0; i < 1000 ; i++) {
            final int index = i ;
            // 不需要返回值，使用execute方法执行Runnable对象
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+":"+index);
                }
            });
            // 需要返回值的时候，使用submit方法执行Callable对象，利用Future对象接收返回值
        }
        threadPool.shutdown();
    }
}
