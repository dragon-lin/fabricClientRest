package com.winyeahs.fabric.sdkinterface;

import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfacePeer extends SdkInterfaceBase {
    // 节点域名
    private String peerName; // peer0.org1.example.com
    // 节点事件域名
    private String peerEventHubName; // peer0.org1.example.com
    // 节点地址
    private String peerLocation; // grpc://110.131.116.21:7051
    // 节点事件监听地址
    private String peerEventHubLocation; // grpc://110.131.116.21:7053
    // 是否增加Event事件处理
    private boolean isEventHub;

    /**
     * 初始化中继Peer对象
     *
     * @param peerName             组织节点域名
     * @param peerEventHubName     组织节点事件域名
     * @param peerLocation         组织节点地址
     * @param peerEventHubLocation 组织节点事件监听访问地址
     * @param isEventHub      是否增加Event事件处理
     */
    SdkInterfacePeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventHub) {
        this.peerName = peerName;
        this.peerEventHubName = peerEventHubName;
        this.peerLocation = peerLocation;
        this.peerEventHubLocation = peerEventHubLocation;
        this.isEventHub = isEventHub;
    }

    public void setPeerName(String peerName) {
        this.peerName = peerName;
    }

    public String getPeerName() {
        return this.peerName;
    }

    public void setPeerEventHubName(String peerEventHubName) {
        this.peerEventHubName = peerEventHubName;
    }

    public String getPeerEventHubName() {
        return this.peerEventHubName;
    }

    public void setPeerLocation(String peerLocation) {
        this.peerLocation = peerLocation;
    }

    public String getPeerLocation() {
        return this.peerLocation;
    }

    public void setPeerEventHubLocation(String peerEventHubLocation) {
        this.peerEventHubLocation = peerEventHubLocation;
    }

    public String getPeerEventHubLocation() {
        return this.peerEventHubLocation;
    }

    public void setIsEventHub(boolean isEventHub) {
        this.isEventHub = isEventHub;
    }

    public boolean getIsEventHub() {
        return this.isEventHub;
    }

}
