package com.winyeahs.fabric.clientrest.service.impl;

import com.winyeahs.fabric.clientrest.mapper.UserMapper;
import com.winyeahs.fabric.clientrest.model.UserModel;
import com.winyeahs.fabric.clientrest.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * Created by linwf on 2018/11/26.
 */
@Service("WebService")
public class WebServiceImpl implements WebService{
    @Autowired
    private UserMapper userMapper;

    public UserModel getUser(String account, String password){
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return userMapper.getUser(account, md5Password);
    }
}
