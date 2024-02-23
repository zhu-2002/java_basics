package thread.threadlocal;

import thread.threadlocal.ThreadSafeFormat.ThreadSafeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 场景1
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

}
