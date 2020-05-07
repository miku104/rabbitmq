package cn.miku.rabbitmq.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 当生产者发送消息到队列中时,队列服务器发送异常,可以使用事务回滚解决数据丢失的问题
 *  1.开启事务机制
 *  2.confirm机制
 * @author yuyuejin
 * @date 2020/5/5 12:50
 */
public class Sender {

    public static final String QUEUE_NAME="test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.103");
        connectionFactory.setVirtualHost("/vhost_miku");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        try {
            channel.txSelect();//相当于在当前开启事务
            //消息发布
            channel.basicPublish("",QUEUE_NAME,null,"Hello traction queue".getBytes());
            int i=1/0;//在提交事务之前,抛出异常
            channel.txCommit();
            System.out.println("消息发送成功...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送消息,出现异常");
            channel.txRollback();//回滚事务
        }

        channel.close();
        connection.close();

    }
}
