package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult contentSave(TbContent tbContent){
        TaotaoResult result = contentService.addContent(tbContent);
        return result;

    }

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult contentList(Long categoryId, Integer page, Integer rows){
        EasyUIDataGridResult result = contentService.contentList(categoryId, page, rows);
        return result;
    }


    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent tbContent) {
        TaotaoResult result = contentService.updateContent(tbContent);
        return result;
    }


    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(String ids) {
        TaotaoResult result = contentService.deleteContent(ids);
        return result;
    }
}
