package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(@RequestParam Integer page, @RequestParam Integer rows){
        EasyUIDataGridResult items = itemService.getItemList(page,rows);
        return items;
    }

    @RequestMapping(value = "/item/save" , method = RequestMethod.POST)
    //加了method就只能响应者一类请求否则可以响应其他任何请求
    @ResponseBody
    public TaotaoResult addItem(TbItem item,String desc){
        TaotaoResult result = itemService.addItem(item, desc);
        return result;
    }
}
