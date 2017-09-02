package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据父节点查询列表
        TbItemCatExample example = new TbItemCatExample();
//        设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
//        设置条件
        criteria.andParentIdEqualTo(parentId);
//        执行转换
        List<EasyUITreeNode> resultList = new ArrayList<>();
        List<TbItemCat> list =itemCatMapper.selectByExample(example);
        for(TbItemCat cat : list){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(cat.getId());
            node.setText(cat.getName());
            node.setState(cat.getIsParent()?"closed":"open");
//            添加到结果列表
            resultList.add(node);
        }
        return resultList;
    }
}
