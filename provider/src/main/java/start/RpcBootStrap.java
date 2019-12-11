package start;

import rpc.RpcProvider;
import service.BatterCakeService;
import service.BatterCakeServiceImpl;

/**
 * Rpc服务器的启动类
 */
public class RpcBootStrap {
    public static void main(String[] args) throws Exception {
        BatterCakeService batterCakeService = new BatterCakeServiceImpl();
        //发布服务，并注册在10099端口
        RpcProvider.export(10099, batterCakeService);
    }
}
