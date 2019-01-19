package com.winyeahs.fabric.clientrest.service.impl;

import com.winyeahs.fabric.clientrest.mapper.FabricConfigMapper;
import com.winyeahs.fabric.clientrest.model.FabricConfigModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigOrdererModel;
import com.winyeahs.fabric.clientrest.sdk.SdkManager;
import com.winyeahs.fabric.clientrest.service.ChainCodeService;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by linwf on 2018/11/3.
 */
@Service("chainCodeService")
public class ChainCodeServiceImpl extends BaseServiceImpl implements ChainCodeService {

    @Autowired
    private FabricConfigMapper fabricConfigMapper;

    @Override
    public String chainCodeInstall() {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.chainCodeInstall();
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }

    @Override
    public String chainCodeInstantiate(String[] args) {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.chainCodeInstantiate(args);
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }

    @Override
    public String chainCodeUpgrade(String[] args) {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.chainCodeUpgrade(args);
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }
    @Override
    public String chainCodeInvoke(String fcn, String[] args) {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.chainCodeInvoke(fcn, args);
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }

    @Override
    public String chainCodeQuery(String fcn, String[] args) {
        SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
        Map<String, String> resultMap;
        try {
            resultMap = manager.chainCodeQuery(fcn, args);
            if (resultMap.get("code").equals("error")) {
                return super.responseFail(resultMap.get("data"));
            } else {
                return super.responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }
}
