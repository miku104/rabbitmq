package cn.miku.rabbitmq.workqueue;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列:
 *  一个队列 多个消费者
 *  只有队列 没有交换机
 *
 *  生产者将消息发送到队列中,然后多个消费者从队列中获取消息,某个消费者将消息接收完了,回应给队列,该消息会删除也在内存中消失,其他消费者接收不到这个消息了的
 * 一个生产者发送消息到队列中,如果有两个消费者并且都是点对点式的发布,那么消费者读取到的消息是你一个我一个,一个读取奇数个,一个读取偶数个,这叫轮询,
 *  这是一个队列多个消费者的点对点式发布
 * @author yuyuejin
 * @date 2020/5/3 21:35
 */
public class WorkQueueSend {

    public static final String QUEUE_NAME="test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i=0;i<50;i++){
            String msg="hello, work queue"+i;
            //发送消息到队列
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            try {
                Thread.sleep(i*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("work queue send ok------------------------------------");
        channel.close();
        connection.close();
    }
    /**
     * 结果:
     *  1.消息内容接收不一样,同一个消息只能被一个消费者读取
     *  2.消息数量一样,一个获取的i是奇数,一个是偶数
     *
     *  为什么呢?我们这里明明做了线程休眠的,消费者1停顿的时间最少,应该获取到的消息数量是比消费者2多的,但是结果就与我们预想的不一样.
     *  这样是为什么捏?
     *      原因:这里不管我们的消费者处理时间多少,队列发送到消费者时,队列就已经得到了消费者的一个回应,队列就以为他是正常处理消息的了
     *      所以不管他停顿时间多久,队列早已把消息论个发送给消费者了,这是得到回应(自动回应)就已经固定好了的,即轮询
     */
}
