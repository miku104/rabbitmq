package cn.miku.rabbitmq.workqueuefair;

import cn.miku.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 公平分发:
 *     某个消费者处理时间多久,处理完之后回应给队列,我已处理完消息,接着推送消息过来,直到没有消息
 *  一个队列多个消费者,
 * @author yuyuejin
 * @date 2020/5/3 21:35
 */
public class WorkQueueSend {

    public static final String QUEUE_NAME="test_work_queue_fair";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        /*
            持久化操作,如果rabbitmq服务挂掉了,那么内存中的数据怎么办呢?我们可以采用将队列消息持久化
            b:true
            细节:
                如果我们一开始并不是持久化操作的,在修改为持久化会报错,原因就是不能持久化已定义好的队列
              异常信息:
                channel error; protocol method:
                #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'durable' for queue 'test_work_queue' in vhost '/vhost_miku':
                 received 'true' but current is 'false', class-id=50, method-id=10)
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        int prefetchCount=10;

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
     *  receive01:
     *  hello, work queue0
     * hello, work queue2
     * hello, work queue4
     * hello, work queue6
     * hello, work queue8
     * hello, work queue10
     * hello, work queue12
     * hello, work queue14
     * hello, work queue16
     * hello, work queue18
     * hello, work queue20
     * hello, work queue22
     * hello, work queue24
     * hello, work queue26
     * hello, work queue28
     * hello, work queue30
     * hello, work queue31
     * hello, work queue32
     * hello, work queue33
     * hello, work queue34                  35个消息
     * hello, work queue35
     * hello, work queue36
     * hello, work queue37
     * hello, work queue38
     * hello, work queue39
     * hello, work queue40
     * hello, work queue41
     * hello, work queue42
     * hello, work queue43
     * hello, work queue44
     * hello, work queue45
     * hello, work queue46
     * hello, work queue47
     * hello, work queue48
     * hello, work queue49
     *
     *
     * receive02:
     * hello, work queue1
     * hello, work queue3
     * hello, work queue5
     * hello, work queue7
     * hello, work queue9
     * hello, work queue11
     * hello, work queue13
     * hello, work queue15
     * hello, work queue17              15个消息
     * hello, work queue19
     * hello, work queue21
     * hello, work queue23
     * hello, work queue25
     * hello, work queue27
     * hello, work queue29
     *
     * 这就是公平分发,根据处理完后发送回应,在读取消息
     */
}
