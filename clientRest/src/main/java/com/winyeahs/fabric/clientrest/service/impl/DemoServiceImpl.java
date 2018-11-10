package com.winyeahs.fabric.clientrest.service.impl;

import com.winyeahs.fabric.clientrest.sdk.SdkManager;
import com.winyeahs.fabric.clientrest.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * Created by linwf on 2018/10/28.
 */
@Service("simpleService")
public class DemoServiceImpl implements DemoService {

    @Override
    public String DemoMethod() {
        SdkManager manager = SdkManager.getInstance();
        return manager.DemoMethod();
    }
}
