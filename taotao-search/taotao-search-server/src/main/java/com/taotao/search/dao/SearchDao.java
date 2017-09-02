package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query)  {
        SearchResult result = new SearchResult();
        QueryResponse response=null;
        try {
            response = solrServer.query(query);
        }catch (SolrServerException e){
            e.printStackTrace();
            return result;
        }
        long numFound = response.getResults().getNumFound();
        //获取总记录数
        result.setRecordCount(numFound);
        List<SearchItem> items = response.getBeans(SearchItem.class);
        //获取高亮显示
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SearchItem item:items){
            //将有高亮显示的加入到item中
            List<String> item_title = highlighting.get(item.getId()).get("item_title");
            if(item_title!=null&&item_title.size()>0){
                item.setTitle(item_title.get(0));
            }
            String img = item.getImage().split(",")[0];
            item.setImage(img);
        }
        System.out.println(items);
        System.out.println(query.getQuery());
        //设置总页数
        Integer rows = query.getRows();
        long pages = numFound / rows;
        if(numFound%rows>0)pages++;
        result.setItemList(items);
        result.setTotalPages(pages);
        return result;
    }
}
