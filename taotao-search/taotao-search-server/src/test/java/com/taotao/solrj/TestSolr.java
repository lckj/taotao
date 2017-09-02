package com.taotao.solrj;

import com.taotao.common.pojo.SearchItem;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolr {

    @Test
    public void testAddDoc() throws Exception{
        //创建一个连接SolrServer，创建一个HttpSolrServer
        //需要指定solr服务的Url
        String url = "http://192.168.13.200:8080/solr/collection1";
        SolrServer solrServer = new HttpSolrServer(url);
        //创建一个文档对象SolrInputDocument
        //向文档中添加域，必须有Id域，名称必须在Schema.xml中定义好
        SolrInputDocument document  = new SolrInputDocument();
        document.addField("id","test001");
        document.addField("item_title","测试商品");
        document.addField("item_price","0");
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }
    @Test
    public void testAddDocBean() throws Exception{
        //创建一个连接SolrServer，创建一个HttpSolrServer
        //需要指定solr服务的Url
        String url = "http://192.168.13.200:8080/solr/collection1";
        SolrServer solrServer = new HttpSolrServer(url);
        //创建一个文档对象SolrInputDocument
        //向文档中添加域，必须有Id域，名称必须在Schema.xml中定义好
        SearchItem document = new SearchItem();
        document.setId("test002");
        document.setCategory_name("测试");
        document.setDesc("用于测试");
        //把文档对象写入索引库
        solrServer.addBean(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocById() throws Exception{
        String url = "http://192.168.13.200:8080/solr/collection1";
        SolrServer solrServer = new HttpSolrServer(url);
        solrServer.deleteById("test002");
        solrServer.commit();
    }

    @Test
    public void deleDocByQuery() throws Exception{
        String url = "http://192.168.13.200:8080/solr/collection1";
        SolrServer solrServer = new HttpSolrServer(url);
//        solrServer.deleteByQuery("id:test002 AND item_price:0");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

    @Test
    public void searchDoc() throws Exception{
        //创建solrserver
        String url = "http://192.168.13.200:8080/solr/collection1";
        SolrServer solrServer = new HttpSolrServer(url);
        //创建query
        SolrQuery query = new SolrQuery();
        //设置查询条件过滤条件排序条件高亮
//        query.set("q","*:*");
        query.setQuery("手机");
        query.setStart(0);
        query.setRows(20);
        query.set("df","item_keywords");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询。得到一个response对象
        QueryResponse response = solrServer.query(query);
        //取得查询结果的总记录数
        long numFound = response.getResults().getNumFound();
        System.out.println("查询结果的总记录数: "+numFound);
        //取得查询结果
//        SolrDocumentList solrDocumentList = response.getResults();
        List<SearchItem> searchItemList = response.getBeans(SearchItem.class);
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for(SearchItem item : searchItemList){
            List<String> item_title = highlighting.get(item.getId()).get("item_title");
            if(item_title!=null&&item_title.size()>0){
                item.setTitle(item_title.get(0));
            }
            System.out.println(item);
        }

    }
}
