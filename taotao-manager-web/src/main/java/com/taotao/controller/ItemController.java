package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *商品管理Controller
 */
@Controller
public class ItemController {

    /**
     * 在配置文件中配置 dubbo的zk注册中心的地址 应用的名称 启用注解
     * <dubbo:application name="taotao-manager-web"/>
     * <dubbo:registry protocol="zookeeper" address="192.168.13.200:2181,192.168.13.201:2181,192.168.13.202:2181"/>
     * <dubbo:annotation package="com.taotao"/>
     */
    @Autowired
    ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId){
        TbItem item = itemService.getItemById(itemId);
        return item;
    }
}
