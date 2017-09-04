package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JedisClient jedisClient;

    @Value("${TBITEM_INF}")
    private String TBITEM_INF;

    @Value("${TIEM_EXPIRE}")
    private Integer TIEM_EXPIRE;

    @Resource(name = "itemAddTopic")
    private Destination destination;

    @Override
    public TbItem getItemById(Long itemId) {
        TbItem tbItem = null;
        //拼装redis中的key
        //缓存的可以组成 TBITEM_INF + ":" + 商品ID + ":" + 信息类型
        String key = TBITEM_INF + ":" + itemId + ":BASE";
        //查询缓存如果没有则查询数据库
        try {
            String json = jedisClient.get(key);
            LOGGER.info("======="+json);
            //判断json是否为空
            if (StringUtils.isNotBlank(json)) {
                //不为空就直接返回
                 tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                //直接返回
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询缓存出错...",e);
        }
        //查询数据库
         tbItem = tbItemMapper.selectByPrimaryKey(itemId);

        //将数据缓存进redis数据库
        cacheData2Redis(key,tbItem);
        return tbItem;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //生成商品ID
        Long itemId = IDUtils.genItemId();
//        补全item商品信息
        item.setId(itemId);
//        商品状态1-正常 2-下架 3-删除
        item.setStatus((byte) 1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
//        向商品表插入
        tbItemMapper.insert(item);
//        创建一个商品描述pojo
        TbItemDesc tbItemDesc = new TbItemDesc();
//        补全pojo信息
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
//        向商品描述表插入
        tbItemDescMapper.insert(tbItemDesc);
        //将商品信息加入索引库
        jmsTemplate.send(destination, session -> {
            TextMessage textMessage = session.createTextMessage(itemId + "");
            return textMessage;
        });

//        返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getTbItemDescById(Long itemId) {
        //拼装redis中的key
        //缓存的可以组成 TBITEM_INF + ":" + 商品ID + ":" + 信息类型
        String key = TBITEM_INF + ":" + itemId + ":DESC";

        //查询缓存如果没有则查询数据库
        try {
            String json = jedisClient.get(key);
            //判断json是否为空
            if (StringUtils.isNotBlank(json)) {
                //不为空就直接返回
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                //直接返回
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询数据库
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //将数据缓存进redis数据库
        cacheData2Redis(key,tbItemDesc);
        return tbItemDesc;
    }

    //缓存数据到redis
    private void cacheData2Redis(String key,Object data){
        try {
            String value = JsonUtils.objectToJson(data);
            jedisClient.set(key, value);
            //设置key的过期时间
            jedisClient.expire(key, TIEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("缓存信息出错...",e);
        }
    }
}
