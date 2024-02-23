package thread.sample;

import java.util.Random;

// 使用Runnable接口创建线程
public class ThreadSample2 {
    class Runner implements Runnable {
        @Override
        public void run() {
            Integer speed = new Random().nextInt(10);
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Thread.currentThread()
                System.out.println("第" + i + "秒：" + Thread.currentThread().getName() + "已跑到" + (i * speed) + "米（" + speed + "米/秒)");
            }

        }
    }
    public void start(){
//        Runner runner = new Runner();
        Thread threadA = new Thread(new Runner()) ;
        threadA.setName("参赛者A");
        Thread threadB = new Thread(new Runner()) ;
        threadB.setName("参赛者B");
        Thread threadC = new Thread(new Runner()) ;
        threadC.setName("参赛者C");
        Thread threadD = new Thread(new Runner()) ;
        threadD.setName("参赛者D");

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
    }

    public static void main(String[] args) {
        new ThreadSample2().start();
        System.out.println("参赛者A 10秒跑了100米");
        System.out.println("参赛者B 10秒跑了60米");
        System.out.println("参赛者C 10秒跑了80米");
    }
}
