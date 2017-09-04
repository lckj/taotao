package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemService itemService;

    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //取出商品Id
            TextMessage textMessage = (TextMessage) message;
            Long itemId = Long.valueOf(textMessage.getText());
            //创建模板
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //获取模型数据
            //等待事务提交
            Thread.sleep(100);
            TbItem itemById = itemService.getItemById(itemId);
            Item item = new Item(itemById);
            TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
            Map<String , Object> data = new HashMap<>();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            //指定生成页面的路径及文件名
            Writer out = new FileWriter(new File(HTML_OUT_PATH+itemId+".html"));
            //生成静态页面
            template.process(data,out);
            //关闭资源
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
