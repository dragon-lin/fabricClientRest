package com.winyeahs.fabric.sdkinterface;

import java.util.Map;

/**
 * Created by linwf on 2018/10/28.
 */
public interface EventListener {
    void received(Map<String, String> map);
}
