package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message)  {
        //取出itemId
        TextMessage textMessage = (TextMessage) message;
        Long itemId = null;
        try {
            itemId = Long.valueOf(textMessage.getText());
            //取数据库查询信息(等待一会,事务提交，否则查询不到数据)
            Thread.sleep(1000);
            SearchItem item = searchItemMapper.getItemById(itemId);
            //拼装SearchItem对象
            solrServer.addBean(item);
            //创建索引
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}
