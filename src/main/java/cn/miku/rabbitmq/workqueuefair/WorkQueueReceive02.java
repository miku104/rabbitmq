package cn.miku.rabbitmq.workqueuefair;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/3 21:46
 */
public class WorkQueueReceive02 {

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
         Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(WorkQueueSend.QUEUE_NAME,true,false,false,null);
        //接收队列传递的最大消息数
        channel.basicQos(15);
        //实例化DefaultConsumer
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //监测队列
                String msg = new String(body, "utf-8");
                System.out.println(msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //已处理消息,回应给队列,队列之后就会删除该消息,注释则会一直存在队列中,rabbit服务挂了也一样存在
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //自定应答
        boolean autoAck = false;
        channel.basicConsume("",autoAck,defaultConsumer);
    }
}
