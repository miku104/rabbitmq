package cn.miku.rabbitmq.springamqp;

/**
 * 消费者
 * @author yuyuejin
 * @date 2020/5/5 16:30
 */
public class Foo  {
    //具体执行业务的方法
    public void listen(String foo) {
        System.out.println("\n消费者： " + foo + "\n");
    }
}
