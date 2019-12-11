package rpc;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Rpc服务器提供器
 */
public class RpcProvider {
    private static List<Object> serviceList;

    /**
     * 发布rpc服务,使用可变参数可以同时发布多个服务，并在服务发布的时候把服务添加到list中
     *
     * @param port
     * @param services
     * @throws Exception
     */
    public static void export(int port, Object... services) throws Exception {
        serviceList = Arrays.asList(services);
        //创建服务器并绑定端口
        ServerSocket server = new ServerSocket(port);
        Socket socket = null;
        while (true) {
            //阻塞等待输入
            socket = server.accept();
            //每一个请求，启动一个线程处理
            new Thread(new ServerThread(socket, serviceList)).start();
        }
    }
}
