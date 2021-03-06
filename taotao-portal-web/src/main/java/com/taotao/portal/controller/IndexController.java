package com.taotao.portal.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String showIndex(Model model){
        //根据cid查询轮播图
        List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
        //把列表转化为AD1Node
        List<AD1Node> ad1Nodes = new ArrayList<>();
        contentList.forEach((content)->{
            AD1Node ad1Node = new AD1Node();
            ad1Node.setAlt(content.getTitleDesc());
            ad1Node.setHeight(AD1_HEIGHT);
            ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setWidthB(AD1_WIDTH_B);
            ad1Node.setSrc(content.getPic());
            ad1Node.setSrcB(content.getPic2());
            ad1Node.setHref(content.getUrl());
            ad1Nodes.add(ad1Node);
        });
        //列表转换成json数据
        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        //把数据传递给页面
        model.addAttribute("ad1",ad1Json);
        return "index";
    }
}
