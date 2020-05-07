package cn.miku.rabbitmq.tx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/5 13:02
 */
public class Receive {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setVirtualHost("/vhost_miku");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(Sender.QUEUE_NAME,false,false,false,null);

        channel.basicConsume(Sender.QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("输出队列中的消息:"+new String(body,"utf-8"));
            }
        });
    }
}
