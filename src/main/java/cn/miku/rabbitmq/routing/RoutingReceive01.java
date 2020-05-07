package cn.miku.rabbitmq.routing;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/4 21:34
 */
public class RoutingReceive01 {

    private static final String QUEUE_NAME="test_routing-queue-1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        channel.queueBind(QUEUE_NAME,RoutingSender.EXCHANGE_NAME,"error");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "utf-8"));
            }
        };
        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);
    }
}
