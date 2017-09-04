package com.taotao.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.TextMessage;

public class SpringActiviMQ {

//    @Test
    public void testJmsTemplateProducer(){
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从容器中获取jmsTemplate
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //获取Destination
        ActiveMQQueue queue = (ActiveMQQueue) applicationContext.getBean("test-queue");
        //发送消息
        jmsTemplate.send(queue,(session -> {
            TextMessage textMessage = session.createTextMessage("spring test queue");
            return textMessage;
        }));
    }

}
