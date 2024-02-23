# Java多线程

> @author	Zenos
>
> 行百里者半九十


## 程序、进程、线程

- **程序**是静态文件，通过可执行文件运行。
- **进程**就是程序开始运行后的一个个实例。
- **线程**是进程内执行的一个基本任务。

## 创建多线程

### 继承Thread类创建线程

```java
package thread;

import java.util.Random;

//继承Thread实现多线程
public class ThreadSample1 {
    class Runner extends Thread{
        @Override
        public void run() {
            Integer speed = new Random().nextInt(10);
            for(int i = 1 ; i <= 10 ; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("第" + i + "秒：" + this.getName() + "已跑到" + (i * speed) + "米（" + speed + "米/秒)");
            }
        }
    }

    public void start(){
        Runner threadA = new Runner();
        threadA.setName("参赛者A");
        Runner threadB = new Runner();
        threadB.setName("参赛者B");
        Runner threadC = new Runner();
        threadC.setName("参赛者C");
        Runner threadD = new Runner();
        threadD.setName("参赛者D");

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
    }

    public static void main(String[] args) {
        new ThreadSample1().start();
        System.out.println("参赛者A 10秒跑了100米");
        System.out.println("参赛者B 10秒跑了60米");
        System.out.println("参赛者C 10秒跑了80米");
    }
}
```

### Runnable接口创建线程

```java
package thread;

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
        Thread threadA = new Thread(runner) ;
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
```

### Callable接口创建线程

```java
package thread;

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
```

### 特点

- **继承Thread类**

  由于Java对继承不友好，不推荐使用。

- **实现Runnable接口**

  对于Java编程友好，但无法返回执行后的数据。

- **实现Callable接口**

  可以返回多个线程中每个线程的执行结果。

## 线程安全

在拥有共享数据的多条线程并行执行的程序中，**线程安全**的代码会通过同步机制保证各个线程都可以正常且正确的执行，不会出现数据污染等意外情况。

### synchronized（同步锁）

**synchronized（同步锁）**关键字的作用就是利用一个特定的对象设置一个lock（锁），在多线程并发访问的时候同时只允许一个线程可以获得这个锁，执行特定的代码。

```java
package thread;

public class SyncSample {
    class Printer {
        Object lock = new Object();
        public void print() throws InterruptedException {
            synchronized (lock) {
                Thread.sleep(1000);
                System.out.print("Java");
                Thread.sleep(1000);
                System.out.print(",");
                Thread.sleep(1000);
                System.out.print("hello ");
                Thread.sleep(1000);
                System.out.println("world !");
            }
        }
    }

    class PrinterTask implements Runnable {
        public Printer printer;

        @Override
        public void run() {
            try {
                printer.print();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() throws InterruptedException {
        Printer printer = new Printer();
        for (int i = 0; i < 10; i++) {
            PrinterTask task = new PrinterTask();
            task.printer = printer;
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SyncSample().start();
    }
}
```

#### synchronized的锁对象

- **synchronized代码块**

  ```java
  // 任意对象即可
  synchronized (Object) {}
  ```

- **synchronized方法**

  ```java
  // this当前对象(使用最多)
  public synchronized void funcName(){}
  ```

- **synchronized的静态方法**

  ```java
  // 该类的字节码对象
  public static synchronized void funcName(){}
  ```

### ThreadLocal

ThreadLocal是一个键值对的线程变量，它通过在每个线程中中创建相同的数据副本，实现数据共享的同时，保证每个线程都可以独立地访问自己的数据，而不会影响其他线程的数据。

#### 原理

![image-20240223104315094](https://cdn.jsdelivr.net/gh/zhu-2002/img/image-20240223104315094.png)

#### 两大使用场景

- 每个线程需要共享一个独享的对象（通常是工具类，典型需要使用的类有SimpleDateFormat和Random）

  ```java
  package thread.threadlocal;
  
  import java.text.SimpleDateFormat;
  import java.util.Date;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  
  public class ThreadLocalNormalUserage00 {
      public static ExecutorService pool = Executors.newFixedThreadPool(10) ;
  
      public static void main(String[] args) throws InterruptedException {
          for (int i = 0; i < 1000; i++) {
              int finalI = i ;
              pool.submit(new Runnable() {
                  @Override
                  public void run() {
                      String date = ThreadLocalNormalUserage00.date(1000*finalI);
                      System.out.println(finalI+"  "+date);
                  }
              });
          }
          pool.shutdown();
      }
      public static String date(int seconds){
          // 单数的单位是ms,从1970.1.1 00：00：00 GMT计时开始
          Date date = new Date(1000 * seconds);
  //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          SimpleDateFormat simpleDateFormat = ThreadSafeFormatter.dateFormat.get();
          return simpleDateFormat.format(date) ;
      }
      class ThreadSafeFormatter{
          public static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
              @Override
              protected SimpleDateFormat initialValue(){
                  return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
              }
          };
      }
  
  }
  ```

- 每个线程内需要保存全局变量（例如在拦截器中获取用户信息），可以让不同方法直接使用，避免参数传递的麻烦

  ```java
  package thread.threadlocal;
  
  import java.text.SimpleDateFormat;
  import java.util.Date;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  
  // 场景2
  /**
   * 描述：     演示ThreadLocal用法2：避免传递参数的麻烦
   */
  public class ThreadLocalNormalUsage01 {
  
      public static void main(String[] args) {
          new Service1().process("");
  
      }
  }
  
  class Service1 {
  
      public void process(String name) {
          User user = new User("超哥");
          UserContextHolder.holder.set(user);
          new Service2().process();
      }
  }
  
  class Service2 {
  
      public void process() {
          User user = UserContextHolder.holder.get();
          ThreadLocalNormalUserage00.ThreadSafeFormatter.dateFormat.get();
          System.out.println("Service2拿到用户名：" + user.name);
          new Service3().process();
      }
  }
  
  class Service3 {
  
      public void process() {
          User user = UserContextHolder.holder.get();
          System.out.println("Service3拿到用户名：" + user.name);
          UserContextHolder.holder.remove();
      }
  }
  
  class UserContextHolder {
  
      public static ThreadLocal<User> holder = new ThreadLocal<>();
  
  
  }
  
  class User {
  
      String name;
  
      public User(String name) {
          this.name = name;
      }
  }
  ```



## 线程池

> 多次新建线程会占用cpu资源，性能较差。而线程缺乏统一管理，线程与线程之间相互竞争，严重时会占用过多系统资源导致死机或内存溢出。线程池是一种管理线程的机制，它可以在应用程序中复用线程，避免了频繁创建和销毁线程的开销。Java中的线程池通过`Executor`来实现。

### 特点

- 复用存在的线程，减少线程对象的创建，消亡的开销。
- 线程总数可控，提高资源的利用率。
- 提供额外的功能，定时执行、定期执行、监控等。

### 状态

- RUNNING

  线程池接受新的任务，并且可以处理已提交的任务

- SHUTDOWN

  线程池不再接受新的任务提交，但会继续处理已提交的任务

- STOP

  此时会中断所有正在执行的任务，并且将等待队列中的任务移除

- TERMINATED

  所有任务都已经执行完成，并且线程池中的所有线程都已经被销毁

### 添加线程规则

1. 线程总数小于核心线程数时，即使有线程处于空闲状态，新服务到来时，也会创建一个新的线程。
2. 线程数等于核心线程数时，再来任务时会把任务放入等待队列。
3. 等待队列满时，线程数小于最大线程数则仍然会创建新线程来执行任务，反之，则会被拒绝。

### 构造方法参数

- corePlloSize（int）

  核心线程数

- maxPoolSize（int）

  最大线程数

- keepAliveTime（long）

  保持存活时间。如果线程池的线程数量多于核心线程数，那么如果多余的线程空闲时间超过keepAliveTime，它们就会被终止。

- workQueue（BlockingQueue）

  任务存储队列。

- threadFactory（ThreadFactory）

  需要新的线程时，使用threadFactory生成新的线程。

  - 默认使用Executors.defaultThreadFactory()
  - 可以自己指定ThreadFactory，改变线程名、线程组、优先级、是否守护线程等

- Handler（RejectedExecutionHandler）

  由于线程池无法接收你所提交的任务的拒绝策略

### 线程池种类

在java.util.concurrent中，提供了工具类Executors对象（调度器对象）来创建线程池，可创建的线程池有四种：

- FixedThreadPool 

  定长线程池

  ```java
  // 定长线程池，参数是线程个数
  ExecutorService threadPool = Executors.newFixedThreadPool(10) ;
  ```

- CachedThreadPool

  可缓存线程池

  ```java
  // 可缓存线程池
  // 无限大，如果线程池中没有可用线程则创建，有空闲线程则用起来
  ExecutorService threadPool = Executors.newCachedThreadPool() ;
  ```

- SigleThreadExecutor

  单线程池

  ```java
  // 单线程池
  ExecutorService threadPool = Executors.newSingleThreadExecutor();
  ```

- ScheduledThreadPool

  调度线程池

  ```java
  package thread.pool;
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
  ```

- newFixedThreadPool

  容易造成大量内存占用可能会导致OOM，out of memory

- newSingleThreadExecutor

  线程数量是1，请求堆积会占用大量内存

- CachedThreadPool

  可缓存线程池，无限大，如果线程池中没有可用线程则创建，有空闲线程则用起来

- ScheduledThreadPool

  支持定时及周期性任务执行的线程池

### 停止线程池的方法

`shutdown()`: 调用此方法会优雅地关闭线程池。它会等待所有已提交的任务执行完成（包括正在执行和还在队列中等待的任务），然后关闭线程池。在调用`shutdown()`之后，线程池不再接受新的任务提交，但会等待之前提交的任务全部执行完成。这样可以保证所有任务都有机会完成，不会被中断。

`shutdownNow()`: 调用此方法会尝试立即关闭线程池。它会尝试停止所有正在执行的任务，并且不再处理等待队列中的任务，返回等待执行的任务列表。但是，这个方法不能保证所有任务都会被执行，因为一些任务可能会被中断或者不支持中断，导致无法正常终止。

```java
package thread.pool;

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

class ShutDownTask implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"被中断了");
        }

    }
}
```

### 暂停和恢复线程池

- 拒绝时机

  1. 当Executor关闭时，提交新的任务会被拒绝
  2. 以及当Excecutor对最大线程和工作队列容量使用有限边界并且已经饱和时

- 四种拒绝策略

  1. AbortPolicy（默认）

     立即拒绝新任务的提交，不会保存任务，直接抛出异常。

  2. DiscardPolicy

     会默默的丢弃，不会通知。

  3. DiscardOldesPolicy

     丢弃线程池队列中最早来的任务。

  4. CallerRunsPolicy

     调用线程来执行被拒绝的任务，如果线程池无法处理新任务，它会将任务交给提交该任务的线程来执行。

- 钩子方法

  - 每个任务执行前后
  
  - 日志、统计

  - 代码演示
  
    ```java
    package thread.pool;
    
    import java.util.concurrent.*;
    import java.util.concurrent.locks.Condition;
    import java.util.concurrent.locks.ReentrantLock;
    
    /**
     *  演示每个任务执行前后方钩子函数
     */
    
    public class PauseableThreadPool extends ThreadPoolExecutor {
    
        private boolean isPaused ;
        private final ReentrantLock lock = new ReentrantLock() ;
        private Condition unpaused  = lock.newCondition();
    
        public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
    
        public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }
    
        public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }
    
        public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            // 上锁
            lock.lock();
            try {
                while (isPaused){
                    // 休眠线程
                    unpaused.await();
                }
            }catch ( Exception e ){
                e.printStackTrace();
            }finally {
                // 开锁
                lock.unlock();
            }
    
        }
    
        private void pause(){
            lock.lock();
            try{
                isPaused = true ;
            }finally {
                lock.unlock();
            }
        }
    
        public void resume(){
            lock.lock();
            try {
                isPaused = false ;
                // 唤醒
                unpaused.signalAll();
            }finally {
                lock.unlock();
            }
        }
    
        public static void main(String[] args) throws InterruptedException {
            PauseableThreadPool pauseableThreadPool = new PauseableThreadPool(10, 20, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("I am running ");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            for (int i = 0; i < 10000 ; i++) {
                pauseableThreadPool.execute(runnable);
            }
            Thread.sleep(1500);
            pauseableThreadPool.pause();
            System.out.println(" pause ");
            Thread.sleep(5000);
            pauseableThreadPool.resume();
        }
    }
    ```
  

## 多线程下载器

### 需求分析

- 通过读取源文件多线程自动下载所有网络资源到本地硬盘。
- 遇到下载故障再控制台上打印错误信息。
- 允许自定义源文件的地址，保存下载文件的目录要自动创建。
- 允许自定义同时下载的任务数量，不指定默认开启10个下载任务。
- 下载文件的文件名要与网址文件名保持一致。
- 下载成功后在控制台输出存储路径与文件尺寸。

### 代码实现思路

1. 读取配置文件
2. 读取源文件
3. 载入List集合
4. 创建线程池
5. 并行批量下载
6. 生成下载报告

## 附录

> 源码链接：E:\workspace\Java_workspace\imooc_learning\src\thread