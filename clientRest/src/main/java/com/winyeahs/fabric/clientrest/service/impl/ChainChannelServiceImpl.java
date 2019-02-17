package com.winyeahs.fabric.clientrest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.winyeahs.fabric.clientrest.mapper.FabricConfigMapper;
import com.winyeahs.fabric.clientrest.sdk.SdkManager;
import com.winyeahs.fabric.clientrest.service.ChainBlockService;
import com.winyeahs.fabric.clientrest.service.ChainChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by linwf on 2018/11/4.
 */
@Service("chainChannelService")
public class ChainChannelServiceImpl extends BaseServiceImpl implements ChainChannelService {
    @Autowired
    private FabricConfigMapper fabricConfigMapper;

    @Override
    public String createChannel() {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.createChannel();
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败:%s", e.getMessage()));
        }
    }

    @Override
    public String joinPeer() {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.joinPeer();
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败:%s", e.getMessage()));
        }
    }

}
