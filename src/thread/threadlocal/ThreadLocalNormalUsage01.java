package thread.threadlocal;

// 场景2

import thread.threadlocal.service.Service1;

/**
 * 描述：     演示ThreadLocal用法2：避免传递参数的麻烦
 */
public class ThreadLocalNormalUsage01 {

    public static void main(String[] args) {
        new Service1().process("");

    }
}

