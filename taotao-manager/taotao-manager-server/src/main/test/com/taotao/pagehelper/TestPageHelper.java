package com.taotao.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {
    public void testPageHelper() throws Exception{
        //1、在mybatis中配置插件
        //2、在查询前配置条件 使用PageHelper静态方法
        PageHelper.startPage(1,10);
        //3、执行查询
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-dao.xml");
        TbItemMapper itemMapper  = applicationContext.getBean(TbItemMapper.class);
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //4、取分页信息 使用pageinfo对象
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("list总记录数："+list.size());
    }

    public static void main(String[] args) throws Exception {
        new TestPageHelper().testPageHelper();
    }
}
