package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;


public interface ContentService {
    EasyUIDataGridResult contentList(Long categoryID, Integer page, Integer rows);
    TaotaoResult addContent(TbContent tbContent);
    TaotaoResult updateContent(TbContent tbContent);
    TaotaoResult deleteContent(String ids);
    List<TbContent> getContentByCid(Long cid);
}
