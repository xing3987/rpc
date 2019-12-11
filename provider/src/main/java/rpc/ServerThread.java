package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

/**
 * ServerThread主要做以下几个步骤
 * <p>
 * 读取客户端发送的服务名
 * 判断服务是否发布
 * 如果发布，则走反射逻辑，动态调用，返回结果
 * 如果未发布，则返回提示通知
 */
public class ServerThread implements Runnable {
    private Socket client = null;
    private List<Object> serviceList = null;

    public ServerThread(Socket client, List<Object> serviceList) {
        this.client = client;
        this.serviceList = serviceList;
    }


    public void run() {
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        try {
            input = new ObjectInputStream(client.getInputStream());
            output = new ObjectOutputStream(client.getOutputStream());

            //读取客户端需要访问那个service(接口类)
            Class serviceClass = (Class) input.readObject();
            //找到该服务类（通过接口类找实现类）
            Object obj = findService(serviceClass);
            if (null == obj) {
                output.writeObject(serviceClass.getName() + "服务未发现。");
            } else {
                //利用反射调用方法
                try {
                    String methodName = input.readUTF();
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                    Object[] argments = (Object[]) input.readObject();
                    Method method = obj.getClass().getMethod(methodName, parameterTypes);
                    Object result = method.invoke(obj, argments);
                    output.writeObject(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    判断父类：父类.class.isAssignableFrom(子类.class)
    判断子类：子类实例 instanceof 父类类型
    */
    private Object findService(Class serviceClass) {
        for (Object service : serviceList) {
            if (serviceClass.isAssignableFrom(service.getClass())) {
                return service;
            }
        }
        return null;
    }
}
