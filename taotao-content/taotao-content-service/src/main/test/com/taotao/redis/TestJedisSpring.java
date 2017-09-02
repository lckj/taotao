package com.taotao.redis;

import com.taotao.jedis.JedisClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
    public static void main(String[] args) {
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //获取jedisClient
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        //利用jedisClient进行操作
        jedisClient.set("jedisSpring","jedis");
        String jedisSpring = jedisClient.get("jedisSpring");
        System.out.println(jedisSpring);

    }
}
