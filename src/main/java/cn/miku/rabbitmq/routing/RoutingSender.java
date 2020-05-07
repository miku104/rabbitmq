package cn.miku.rabbitmq.routing;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式,在订阅模式的基础上添加过滤功能
 * @author yuyuejin
 * @date 2020/5/4 21:34
 */
public class RoutingSender {

    public static final String EXCHANGE_NAME="test_exchange_direct_routing";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取通道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        String msg="hello direct routing.........";
        //设置routingKey:即可将消息发送到交换机中时,交换机会根据路由键指定将消息推送到某个队列中,这样可以过滤掉某些不需要消息的队列
        // RoutingReceive01只会接受路由键 "error" 的消息推送
        //RoutingReceive02不管error,info,warning 级别的消息推送都会接收消息
        channel.basicPublish(EXCHANGE_NAME,"error",null,msg.getBytes());
        System.out.println("message send ok............");
        channel.close();
        connection.close();
    }
}
