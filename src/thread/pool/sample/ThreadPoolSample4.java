package thread.pool.sample;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolSample4 {
    public static void main(String[] args) {
        // 调度程池
        // 参数是线程池的基本大小，
        // 即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
        // 延迟1秒执行，每3秒执行1次
        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println( new Date() + "延迟1秒执行，每3秒执行1次");
            }
        },1,3, TimeUnit.SECONDS);
    }
}
