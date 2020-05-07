package cn.miku.rabbitmq.simple;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息消费者
 * @author yuyuejin
 * @date 2020/5/2 22:22
 */
public class SimpleReceive {

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(SimpleSend.QUEUE_NAME,false,false,false,null);
        //定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //重写handlerDelivery方法,自定义获取消息内容并处理
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String content = new String(body, "utf-8");
                System.out.println(SimpleSend.QUEUE_NAME + "队列内容:" + content);
            }
        };
        channel.basicConsume(SimpleSend.QUEUE_NAME,true,consumer);
    }
}
