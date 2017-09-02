package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class ContentCategoryControoler {

    @Autowired
    private ContentCategoryService categoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue ="0")Long parentId){
        List<EasyUITreeNode> categoryList = categoryService.getContentCategoryList(parentId);
        return categoryList;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(@RequestParam Long parentId,@RequestParam String name){
        TaotaoResult result = categoryService.addContentCategory(parentId, name);
        return result;
    }

    @RequestMapping("/content/category/update")
    @ResponseBody
    public void updateContentCategory(@RequestParam Long id,@RequestParam String name){
        categoryService.updateContentCategory(id, name);

    }

    @RequestMapping("/content/category/delete")
    @ResponseBody
    public void deleteContentCategory(@RequestParam Long id){
        categoryService.deleteContentCategory(id);
    }

}
