package com.winyeahs.fabric.sdkinterface;

import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceOrderer extends SdkInterfaceBase {
    // orderer 排序服务器的域名
    private String ordererName;
    // orderer 排序服务器地址
    private String ordererLocation;

    SdkInterfaceOrderer(String ordererName, String ordererLocation) {
        super();
        this.ordererName = ordererName;
        this.ordererLocation = ordererLocation;
    }

    public void setOrdererName(String ordererName) {
        this.ordererName = ordererName;
    }

    public String getOrdererName() {
        return ordererName;
    }

    public void setOrdererLocation(String ordererLocation) {
        this.ordererLocation = ordererLocation;
    }

    public String getOrdererLocation() {
        return ordererLocation;
    }
}
