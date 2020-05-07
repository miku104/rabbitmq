package cn.miku.rabbitmq.subscribe;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/4 20:45
 */
public class SubscribeReceive02 {

    private static final String QUEUE_NAME="test_publish_subscribe_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,SubscribeSender.EXCHANGE_NAME,"");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "utf-8"));
            }
        };
        channel.basicConsume(queueName,true,defaultConsumer);
    }
}
