package cn.miku.rabbitmq.subscribe;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/4 20:45
 */
public class SubscribeReceive01 {

    private static final String QUEUE_NAME="test_publish_subscribe_queue_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //将交换机和队列进行绑定   队列名         交换名                 路由键
        channel.queueBind(QUEUE_NAME,SubscribeSender.EXCHANGE_NAME,"");

        channel.basicQos(1);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);
    }
}
