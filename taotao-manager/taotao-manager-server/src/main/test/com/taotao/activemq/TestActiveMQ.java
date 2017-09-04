package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.util.Scanner;

public class TestActiveMQ {
    /**
     * 大体的思路是
     * ConnectionFactory-》connection=》session=》Queue（Destination）、producer、textMessage=》producer发送textMessage
     *
     * @throws Exception
     */
//    @Test
    public void testQueueMqProducer() throws Exception {
        //1、创建一个ConnectionFactory对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.13.110:61616");
        //2、ConnectionFactory使用创建一个Connect对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、使用Connect对象穿件Session
        //4.1、第一个参数是是否开启事务，一般不适用事务，保证消息最终一致，可以使用消息队列实现
        //4.2、如果第一个参数是true那么第二个参数可以忽略
        //4.3、如果第一个是false，那么第二个参数是应答模式（自动，手动），一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用Session创建Destination，两种形式queue；topic
        Queue queue = session.createQueue("test-queue");//参数当前消息队列的名称
        //6、使用Session创建生产者Producer对象
        MessageProducer producer = session.createProducer(queue);
        //7、创建一个TextMessage对象
        //7.1方式一
//        TextMessage textMessage = new ActiveMQTextMessage();
//        textMessage.setText("hello activemq");
        //7.2方式二
        TextMessage textMessage = session.createTextMessage("hello activemq");
        //8、发布消息
        producer.send(textMessage);
        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /**
     * 大体思路 ConnectionFactory=》Connection=》Session=》Destination、MessageConsumer、TextMessage=》处理TextMessage
     *
     * @throws Exception
     */
//    @Test
    public void testQueueConsumer() throws Exception {
        //创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.13.110:61616");
        //创建连接Connect
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建Destination
        Queue queue = session.createQueue("test-queue");
        //创建一个消费者Consumer
        MessageConsumer consumer = session.createConsumer(queue);
        //向Consumer设置一个MessageListener
        consumer.setMessageListener((msg) -> {
            //获取消息
            if (msg instanceof TextMessage) {
                TextMessage msg1 = (TextMessage) msg;
                try {
                    String text = msg1.getText();
                    //打印消息
                    System.out.println("consumer接受消息：" + text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //系统等待
//        while (true){
//            Thread.sleep(1000);
//        }
//        System.in.read();
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    /**
     * TopicProducer发送消息时，消费端必须是启动状态的，否则消息发送后默认就没有了；在也消费不到了
     *
     * @throws Exception
     */
//    @Test
    public void testTopicProducer() throws Exception {
        //创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.13.110:61616");
        //创建Connect
        Connection connection = connectionFactory.createConnection();
        //创建一个Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个Destination（Topic）
        Topic topic = session.createTopic("test-topic");
        //创建一个Producer
        MessageProducer producer = session.createProducer(topic);
        //创建一个TextMessage
        TextMessage textMessage = session.createTextMessage("hello topic message");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }

//    @Test
    public void testTopicConsumer()throws  Exception {
        //创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.13.110:61616");
        //创建Connection
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个Destination（Topic）
        Topic topic = session.createTopic("test-topic");
        //创建一个Consumer
        MessageConsumer consumer = session.createConsumer(topic);
        //为consumer设置一个ConsumerListener
        consumer.setMessageListener((msg)->{
            //获取消息
            if (msg instanceof TextMessage) {
                TextMessage msg1 = (TextMessage) msg;
                try {
                    String text = msg1.getText();
                    //打印消息
                    System.out.println("consumer接受消息：" + text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();

    }

}
