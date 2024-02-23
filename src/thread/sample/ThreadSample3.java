package thread.sample;

import java.util.Random;
import java.util.concurrent.*;

public class ThreadSample3 {
    class Runner implements Callable<Integer>{
        public String name ;
        @Override
        public Integer call() throws Exception {
            Integer speed =  new Random().nextInt(10);
            Integer res =  0 ;
            for (int i = 1; i < 11; i++) {
                Thread.sleep(1000);
                res = i* speed ;
                System.out.println("第" + i + "秒：" + this.name+ "已跑到" + (i * speed) + "米（" + speed + "米/秒)");
            }
            return res ;
        }
    }
    public void start() throws ExecutionException, InterruptedException {
        // 定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Runner threadA = new Runner() ;
        threadA.name = "参赛者A" ;
        Runner threadB = new Runner() ;
        threadB.name = "参赛者B" ;
        Runner threadC = new Runner() ;
        threadC.name = "参赛者C" ;
        // 线程运行的结果是Future类
        Future<Integer> runner1 = executorService.submit(threadA) ;
        Future<Integer> runner2 = executorService.submit(threadB) ;
        Future<Integer> runner3 = executorService.submit(threadC) ;
        // 所有线程任务结束后，关闭线程池
        executorService.shutdown();
        System.out.println(threadA.name+"累计跑了"+runner1.get()+"米");
        System.out.println(threadB.name+"累计跑了"+runner2.get()+"米");
        System.out.println(threadC.name+"累计跑了"+runner3.get()+"米");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ThreadSample3().start() ;
    }
}
