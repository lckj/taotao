package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importItemToIndex()  {
        TaotaoResult result =null;
        //查询所有商品
        List<SearchItem> itemList = searchItemMapper.getItemList();
        //遍历商品数据到索引
        //创建文档对象
        //把文档写入索引
        try {
            solrServer.addBeans(itemList);
            //提交
            solrServer.commit();
            result = TaotaoResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            result = TaotaoResult.build(500,"数据导入失败");
        }
        //返回
        return result;
    }
}
