package cn.miku.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * 异步模式:
 * Channel对象提供的Confirmlistener()回调方法只包含deliveryTag (当前Chanel发出的消息序号),
 * 我们需要自己为每一个Channel维护一个unconfirm的消息序号集合,每publish一条数据,集合中元素加1,每回调一次handleAck方法,
 * unconfirm集合删掉相应的一条(multiple-false)或多条(multiple=true)记录。从程序运行效率上看,这个unconfirm集合最好采用有序集合SortedSet存储结构.
 * @author yuyuejin
 * @date 2020/5/5 14:06
 */
public class AnsynchronizedSender03 {


    public static final String QUEUE_NAME="test_queue_comfirm-3";

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

        //未确认的消息
        SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        channel.addConfirmListener(new ConfirmListener() {

            //没有问题的handleAck
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("----handleAck----multiple");
                    confirmSet.headSet(deliveryTag+1).clear();
                } else {
                    System.out.println("----handleAck----multiple----false");
                    confirmSet.remove(deliveryTag);
                }
            }

            //handleNack
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("----handleNack----multiple");
                    confirmSet.headSet(deliveryTag+1).clear();
                }else{
                    System.out.println("----handleNack----multiple----false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

       while(true){
           //获取消息的序号
           Long seqNo=channel.getNextPublishSeqNo();
           channel.basicPublish("",QUEUE_NAME,null,"hello ansynchronized ..........".getBytes());
           //将序号添加到SortSet集合中
           confirmSet.add(seqNo);
       }
    }
}
