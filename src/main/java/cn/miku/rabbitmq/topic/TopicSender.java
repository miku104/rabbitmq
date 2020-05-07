package cn.miku.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jdk.nashorn.internal.ir.debug.ClassHistogramElement;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主题模式:
 *
 * @author yuyuejin
 * @date 2020/5/4 22:45
 */
public class TopicSender {

    public static final String EXCHANGE_NAME="test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/vhost_miku");
        factory.setHost("192.168.0.103");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明交换机 使用主题模式
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String msg="商品:删除.......";
        //第一消息会失效,因为并没有队列
        channel.basicPublish(EXCHANGE_NAME,"goods.add",null,msg.getBytes("utf-8"));

        //关闭通道关闭连接
        channel.close();
        connection.close();

    }
}
