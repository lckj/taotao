package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {
    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page, Model model){
//        int a = 1/0;
        //get请求参数转码
        try {
            queryString = new String(queryString.getBytes("iso8859-1"), "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        SearchResult result = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
        model.addAttribute("query",queryString);
        model.addAttribute("totalPages",result.getTotalPages());
        model.addAttribute("itemList",result.getItemList());
        logger.info(result.getItemList().toString());
        model.addAttribute("page",page);
        //返回逻辑视图
        return "search";
    }

}
