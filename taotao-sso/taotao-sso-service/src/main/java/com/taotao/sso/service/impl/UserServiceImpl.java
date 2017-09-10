package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_KEY}")
    private String SESSION_KEY;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String data, int type) {
        TaotaoResult result = null;
        //创建查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //根据传入的type来确定创建的条件
        if(type==USERNAME){
            criteria.andUsernameEqualTo(data);
        }else if(type == PHONENUM){
            criteria.andPhoneEqualTo(data);
        }else if(type == EMAIL){
            criteria.andEmailEqualTo(data);
        }else{
            result = TaotaoResult.build(400, "参数中type为非法数据");
            return result;
        }
        //查询
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        //数据库中已有对应的用户
        if(tbUsers!=null&&tbUsers.size()>0){
            result = TaotaoResult.ok(false);
            return result;
        }
        //数据可用
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        //校验数据
        if(StringUtils.isBlank(user.getUsername())){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        //检查用户名是否重复
        TaotaoResult result = checkData(user.getUsername(), USERNAME);
        if(!(boolean)result.getData()){
            return TaotaoResult.build(400,"用户名重复");
        }

        //检查密码
        if(StringUtils.isBlank(user.getPassword())){
            return TaotaoResult.build(400,"密码不能为空");
        }

        //当电话号码不为空时，检查是否重复
        if(!StringUtils.isBlank(user.getPhone())){
             result = checkData(user.getPhone(), PHONENUM);
             if(!(boolean)result.getData()){
                 return TaotaoResult.build(400,"电话号码重复");
             }
        }

        //当email不为空时，检查是否重复
        if(!StringUtils.isBlank(user.getEmail())){
            result = checkData(user.getEmail(), EMAIL);
            if(!(boolean)result.getData()){
                return TaotaoResult.build(400,"电子邮箱重复");
            }
        }

        //补全数据
        Date date = new Date();
        user.setUpdated(date);
        user.setCreated(date);
        //密码进行MD5加密
        String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pwd);
        //插入数据
        tbUserMapper.insert(user);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //根据用户名查询用户信息
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        //判断用户是否存在
        if(tbUsers==null||tbUsers.size()==0){
            return TaotaoResult.build(400,"用户或密码不正确！");
        }
        TbUser tbUser = tbUsers.get(0);
        //用MD5加密密码
        String pwdMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //判断用户密码是否正确
        if(!pwdMd5.equals(tbUser.getPassword())){
            return TaotaoResult.build(400,"用户或密码不正确！");
        }
        //生成token
        String token = UUID.randomUUID().toString();
        //将用户信息用户信息写入redis
        tbUser.setPassword(null);
        String key = SESSION_KEY+":"+token;
        jedisClient.set(key, JsonUtils.objectToJson(tbUser));
        //设置过期时间
        jedisClient.expire(key,SESSION_EXPIRE);
        //返回结果
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String key = SESSION_KEY+":"+token;
        //从redis中查询token对应的user
        String json = jedisClient.get(key);
        //判断token是否有效
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"token无效已过期！");
        }
        //重置session
        jedisClient.expire(key,SESSION_EXPIRE);
        //返回结果 此时不能直接返回json 因为json中有"" 在TaotaoResult中ok方法还会在吧json转换为json这样结果中就会多出\"
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult logout(String token) {
        String key = SESSION_KEY+":"+token;
        //到redis中删除token
        if(jedisClient.exists(key)){
            jedisClient.del(key);
        }
        //删除成功返回结果
        return TaotaoResult.ok();
    }
}
