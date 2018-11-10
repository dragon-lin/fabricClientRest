package com.winyeahs.fabric.clientrest.service.impl;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by linwf on 2018/11/3.
 */
public class BaseServiceImpl {
    int SUCCESS = 200;
    int FAIL = 40029;

    protected String responseSuccess(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", SUCCESS);
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    protected String responseSuccess(String result, String txid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", SUCCESS);
        jsonObject.put("result", result);
        jsonObject.put("txid", txid);
        return jsonObject.toString();
    }

    protected String responseSuccess(JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", SUCCESS);
        jsonObject.put("data", json);
        return jsonObject.toString();
    }

    protected String responseFail(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", FAIL);
        jsonObject.put("error", result);
        return jsonObject.toString();
    }
}
