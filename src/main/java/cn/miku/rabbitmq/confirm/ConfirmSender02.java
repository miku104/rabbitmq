package cn.miku.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Confirm模式:
 *   生产者将信道设置成confirm模式,一旦信道进入confirm模式,所有在该信道上面发布的消息都会被指派一个唯一的ID(从1开始),
 *  一旦消息被投递到所有匹配的队列之后,broker就会发送一个确认给生产者(包含消息的唯一ID),这就使得生产者知道消息已经正确到达目的队列了,
 *  如果消息和队列是可持久化的,那么确认消息会将消息鸟入磁盘之后发出, broker回传给生产者的确认消息中deliver-tag域包含了确认消息的序列号,
 *  此外broker也可以设置basic.ack的multiple域,表示到这个序列号之前的所有消息都已经得到了处理。
 * @author yuyuejin
 * @date 2020/5/5 14:06
 */
public class ConfirmSender02 {

    public static final String QUEUE_NAME="test_queue_comfirm-2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setVirtualHost("/vhost_miku");

        Connection connection = connectionFactory.newConnection();

        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //生产者调用功能confirmSelect()方法,将channel设置为Confirm模式。    注意:事务机制和Confirm机制只能选择一个,否则会发生异常
        channel.confirmSelect();

        /**
         * 批量发送多条消息,一个消息发送失败,则整个批量发送的消息都不会发送到队队列
         */
        for(int i=0;i<10;i++){
            channel.basicPublish("",QUEUE_NAME,null,"hello comfirm..........".getBytes());
        }

        if(channel.waitForConfirms()){
            System.out.println("message send success---------消息发送成功");
        }else{
            System.out.println("message send failed---------消息发送失败");
        }
        channel.close();
        connection.close();
    }
}
