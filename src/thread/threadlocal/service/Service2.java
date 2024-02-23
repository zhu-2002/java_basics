package thread.threadlocal.service;

import thread.threadlocal.ThreadSafeFormat.ThreadSafeFormatter;
import thread.threadlocal.user.*;

public class Service2 {

    public void process() {
        User user = UserContextHolder.holder.get();
        ThreadSafeFormatter.dateFormat.get();
        System.out.println("Service2拿到用户名：" + user.name);
        new Service3().process();
    }
}
