package com.taotao.solrj;

import com.taotao.common.pojo.SearchItem;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.junit.Test;

public class TestCloudSolr {
//    @Test
    public void testCloud() throws Exception{
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.13.200:2181,192.168.13.201:2181,192.168.13.202:2181");
        cloudSolrServer.setDefaultCollection("collection2");
        SearchItem item = new SearchItem();
        item.setId("95277");
        item.setTitle("测试商品");
        item.setPrice(100);
        cloudSolrServer.addBean(item);
        cloudSolrServer.commit();
    }
}
