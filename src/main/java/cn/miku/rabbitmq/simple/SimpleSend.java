package cn.miku.rabbitmq.simple;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 点对点模式:只有队列,没有交换机,生产者将消息直接发送到队列中,然后消费者从队列中获取消息
 * 消息生产者
 * @author yuyuejin
 * @date 2020/5/2 22:21
 */
public class SimpleSend {
    //一个队列名
    public static  String QUEUE_NAME="test-simple-queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //内容
        String msg="Hello Rabbitmq,行百里者半九十";

        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        System.out.println("生产者发送消息到队列中------");
        //关闭通道
        //关闭连接
        channel.close();
        connection.close();
    }
}
