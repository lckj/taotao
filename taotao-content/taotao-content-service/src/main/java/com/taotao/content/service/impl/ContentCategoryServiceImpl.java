package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {


    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categoryList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //转换为easyuinode
        List<EasyUITreeNode> result = new ArrayList<>();
        for (TbContentCategory category : categoryList){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent()?"closed":"open");
            //添加到结果
            result.add(node);
        }
        return result;
    }

    @Override
    public TaotaoResult addContentCategory(Long parentId, String name) {
        //创建pojo对象
        TbContentCategory category = new TbContentCategory();
        //补全pojo对象
        category.setParentId(parentId);
        category.setName(name);
        //状态 1-正常 2-删除
        category.setStatus(1);
        //排序默认为1
        category.setSortOrder(1);
        category.setIsParent(false);
        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        //插入到数据库
        tbContentCategoryMapper.insert(category);
        //返回父节点状态
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            //如果父节点为叶子节点改为父节点
            parent.setIsParent(true);
            //更新父节点
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok(category);
    }

    @Override
    public void updateContentCategory(Long id, String name) {
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
        category.setName(name);
        tbContentCategoryMapper.updateByPrimaryKey(category);
    }

    @Override
    public void deleteContentCategory(Long id) {
        //查询节点
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
        //判断节点是否为父节点
        if(category.getIsParent()){
            //是父节点,获取叶子节点
            //创建查询条件
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            //查询叶子节点
            List<TbContentCategory> categories = tbContentCategoryMapper.selectByExample(example);
            categories.forEach((c)->deleteContentCategory(c.getId()));
        }
        tbContentCategoryMapper.deleteByPrimaryKey(id);


    }

}
