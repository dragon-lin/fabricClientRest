package com.winyeahs.fabric.clientrest.service.impl;

import com.winyeahs.fabric.clientrest.mapper.FabricConfigMapper;
import com.winyeahs.fabric.clientrest.model.FabricConfigModel;
import com.winyeahs.fabric.clientrest.sdk.SdkManager;
import com.winyeahs.fabric.clientrest.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by linwf on 2018/10/28.
 */
@Service("simpleService")
public class DemoServiceImpl implements DemoService {
    @Autowired
    private FabricConfigMapper fabricConfigMapper;

    @Override
    public String DemoMethod() {
        //SdkManager manager = SdkManager.getInstance();
        //return manager.DemoMethod();
        List<FabricConfigModel> fabricConfigModels = fabricConfigMapper.queryFabricConfig("1");
        return "";
    }
}
