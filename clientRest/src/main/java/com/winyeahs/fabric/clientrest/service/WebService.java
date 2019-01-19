package com.winyeahs.fabric.clientrest.service;

import com.winyeahs.fabric.clientrest.model.UserModel;

/**
 * Created by linwf on 2018/11/26.
 */
public interface WebService {
    UserModel getUser(String account, String password);
}
