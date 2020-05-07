package cn.miku.rabbitmq.subscribe;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布/订阅模式:
 *      一个消息生产者,多个队列,多个消费者,读取的消息可以一样
 * @author yuyuejin
 * @date 2020/5/3 23:34
 */
public class SubscribeSender {

    //交换机name
    public static final String EXCHANGE_NAME="test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换机                                 订阅模式:不处理路由键
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String msg="hello exchagne";    //  路由键不被处理,所以为""
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

        //关闭通道
        //关闭连接
        channel.close();
        connection.close();
    }
}
