package thread.threadlocal.service;

import thread.threadlocal.user.*;

public class Service1 {

    public void process(String name) {
        User user = new User("超哥");
        UserContextHolder.holder.set(user);
        new Service2().process();
    }
}
