package com.winyeahs.fabric.clientrest.service;

/**
 * Created by linwf on 2018/11/3.
 */
public interface ChainCodeService {
    String chainCodeInstall();
    String chainCodeInstantiate(String[] args);
    String chainCodeUpgrade(String[] args);
    String chainCodeInvoke(String fcn, String[] args);
    String chainCodeQuery(String fcn, String[] args);
}
