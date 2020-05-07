package cn.miku.rabbitmq.workqueuefair;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/3 21:46
 */
public class WorkQueueReceive01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
         Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(WorkQueueSend.QUEUE_NAME,true,false,false,null);
        //接收队列传递的最大消息数
        //同一时刻服务器只会发15条消息给消费者
        channel.basicQos(15);
        //实例化DefaultConsumer
        Consumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                //监测队列
                String msg = new String(body, "utf-8");
                System.out.println(msg);
                try {
                    Thread.sleep(1000);

                    //已处理消息,回应给队列,队列之后就会删除该消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        /*
        b:自定应答,
        false:当我们处理完了数据,如果没有回应给队列,那么他会一直存在与队列中,不会删除队列中的消息的,除非队列挂掉了,挂掉了的话消息依旧在的,因为队列没有执行删除消息操作,
        不过回应给队列就可以删除消息了
        true:自动回应队列,队列将消息推送给消费者,然后就会删除消息
         */

        boolean autoAck = false;
        channel.basicConsume("",autoAck,defaultConsumer);
    }
}
