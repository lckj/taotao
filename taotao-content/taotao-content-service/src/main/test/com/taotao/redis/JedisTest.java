package com.taotao.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {


    public void  jedisPool(){
        JedisPool pool = new JedisPool("192.168.13.200",6379);//单例
        Jedis jedis = pool.getResource();//方法级别
        jedis.set("str3","world");
        jedis.close();
    }

    public static void main(String[] args) {
//        Jedis jedis = new Jedis("192.168.13.200",6379);
//        jedis.set("str2","hello");
//        jedis.close();
//        JedisPool pool = new JedisPool("192.168.13.200",6379);//单例
//        Jedis jedis = pool.getResource();//方法级别
//        jedis.set("str3","world");
//        jedis.close();
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.13.200",6379));
        nodes.add(new HostAndPort("192.168.13.200",6378));
        nodes.add(new HostAndPort("192.168.13.201",6379));
        nodes.add(new HostAndPort("192.168.13.201",6378));
        nodes.add(new HostAndPort("192.168.13.202",6379));
        nodes.add(new HostAndPort("192.168.13.202",6378));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("cluster-test","hello redis cluster");
        String s = jedisCluster.get("cluster-test");
        System.out.println(s);
    }
}
