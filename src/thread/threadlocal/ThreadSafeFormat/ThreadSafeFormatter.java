package thread.threadlocal.ThreadSafeFormat;

import java.text.SimpleDateFormat;

public class ThreadSafeFormatter{
        public static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
            @Override
            protected SimpleDateFormat initialValue(){
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
        };
    }