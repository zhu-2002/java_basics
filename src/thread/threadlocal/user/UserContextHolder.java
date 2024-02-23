package thread.threadlocal.user;

public class UserContextHolder {

    public static ThreadLocal<User> holder = new ThreadLocal<>();


}
