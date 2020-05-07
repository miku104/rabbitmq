package cn.miku.rabbitmq.utils;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yuyuejin
 * @date 2020/5/2 21:56
 */
public class ConnectionUtils {

    /**
     * 获取MQ连接
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义一个连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //设置服务IP地址
        connectionFactory.setHost("192.168.0.103");
        //AMQP 客户端端口号
        connectionFactory.setPort(5672);
        //vhost
        connectionFactory.setVirtualHost("/vhost_miku");
        //用户名
        connectionFactory.setUsername("miku");
        //密码
        connectionFactory.setPassword("miku");
        //new 一个连接
        return connectionFactory.newConnection();
    }
}
