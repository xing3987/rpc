package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 代理请求类
 */
public class ProxyHandler implements InvocationHandler {

    private String ip;
    private Integer port;

    public ProxyHandler(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * invoke三个参数：
     * <p>
     * proxy：就是代理对象，newProxyInstance方法的返回对象
     * <p>
     * method：调用的方法
     * <p>
     * args: 方法中的参数
     * <p>
     * 代理对象，调用run方法时，自动执行invocationhandler中的invoke方法。
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket(this.ip, this.port);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        try {
            //提供数据给服务发行者，这里写出的数据要和服务端拿取的数据一致

            //getInterfaces()方法的到该类的所有接口
            output.writeObject(proxy.getClass().getInterfaces()[0]);
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
            output.flush();
            Object result = input.readObject();
            //如果获取到了异常获取错误，则抛出
            if (result instanceof Throwable) {
                throw (Throwable) result;
            }
            return result;
        } finally {
            socket.shutdownOutput();
        }
    }
}
