package cn.miku.rabbitmq.springamqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * rabbitmq集成spring
 * 生产者
 * @author yuyuejin
 * @date 2020/5/5 16:30
 */
public class producer {
    public static void main(final String... args) throws Exception {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:spring/rabbitmq-context.xml");
        //RabbitMQ模板
        RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        //发送消息
        template.convertAndSend("Hello, Miku!");
        template.setRoutingKey("");
        Thread.sleep(1000);// 休眠1秒
        ctx.close();;//容器销毁
    }
}
