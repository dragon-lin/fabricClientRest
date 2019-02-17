package com.winyeahs.fabric.clientrest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    protected String responseSuccess(String result, String txid) throws IOException {
        ObjectNode resRootNode = JsonNodeFactory.instance.objectNode();
        resRootNode.put("status", SUCCESS);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode resData = mapper.readTree(result);
            resRootNode.put("data", resData);
        }catch (IOException e) {
            resRootNode.put("data", result);
        }
        resRootNode.put("txid", txid);
        return resRootNode.toString();

        /*
        JSONObject jsonObject = new JSONObject();
        String jsonStr = String.format("{\"data\":%s,\"txid\":\"%s\"}", result, txid);
        JSONObject jsonObject1 = JSONObject.parseObject(jsonStr);
        jsonObject.put("status", SUCCESS);
        jsonObject.put("result", result);
        jsonObject.put("txid", txid);
        return jsonObject.toString();
        */

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
