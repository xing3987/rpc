package start;

import rpc.RpcConsumer;
import service.BatterCakeService;

/**
 * 测试调用服务器
 */
public class RpcTest {
    public static void main(String[] args) {
        //链接服务器，准备调用服务器的接口类
        BatterCakeService service = RpcConsumer.getService(BatterCakeService.class, "localhost", 10099);
        String result = service.sellBatterCake("好吃");
        System.out.println(result);
    }
}
