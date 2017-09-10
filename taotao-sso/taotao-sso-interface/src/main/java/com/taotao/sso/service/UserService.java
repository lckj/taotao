package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    int EMAIL = 3;
    int PHONENUM = 2;
    int USERNAME = 1;

    TaotaoResult checkData(String data, int type);
    TaotaoResult register(TbUser user);
    TaotaoResult login(String username,String password);
    TaotaoResult getUserByToken(String token);
    TaotaoResult logout(String token);
}
