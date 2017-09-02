package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setUpdated(date);
        tbContent.setCreated(date);
        tbContentMapper.insert(tbContent);
        //刷新缓存
        jedisClient.hdel(INDEX_CONTENT,tbContent.getCategoryId().toString());
       return TaotaoResult.ok();
    }

    @Override
    public EasyUIDataGridResult contentList(Long categoryId, Integer page, Integer rows) {

        //设置分页信息
        PageHelper.startPage(page,rows);
        //创建查询条件
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult updateContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setUpdated(date);
        tbContentMapper.updateByPrimaryKey(tbContent);
        TaotaoResult result = TaotaoResult.ok();
        //刷新缓存
        jedisClient.hdel(INDEX_CONTENT,tbContent.getCategoryId().toString());
        return result;
    }

    @Override
    public TaotaoResult deleteContent(String ids) {
        String[] ss = ids.split(",");
        TbContent tbContent=null;
        for(String s:ss){
            Long id = Long.valueOf(s);
            tbContent = tbContentMapper.selectByPrimaryKey(id);
            tbContentMapper.deleteByPrimaryKey(id);
        }
        TaotaoResult result = TaotaoResult.ok();
        //刷新缓存
        if(tbContent!=null) {
            jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
        }
        return result;
    }

    @Override
    public List<TbContent> getContentByCid(Long cid) {

        //查缓存
        try {
            //查询到结果
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        //将结果缓存
        try {
            jedisClient.hset(INDEX_CONTENT,cid+"", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
