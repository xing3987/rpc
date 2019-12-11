package rpc;

import java.lang.reflect.Proxy;

/**
 * rpc消费者
 */
public class RpcConsumer {

    public static <T> T getService(Class<T> clazz, String ip, int port) {
        ProxyHandler proxyHandler = new ProxyHandler(ip, port);
        //newProxyInstance，方法有三个参数：
        //
        //loader: 用哪个类加载器去加载代理对象
        //
        //interfaces:动态代理类需要实现的接口
        //
        //h:动态代理方法在执行时，会调用h里面的invoke方法去执行
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, proxyHandler);
    }
}
