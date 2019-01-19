package com.winyeahs.fabric.clientrest.mapper;

import com.winyeahs.fabric.clientrest.model.UserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by linwf on 2018/11/26.
 */
public interface UserMapper {
    /**
     * 获取用户
     * @param account 帐户
     * @param password 密码
     * @return
     */
    public UserModel getUser(@Param("account") String account, @Param("password") String password);
}
