package com.winyeahs.fabric.clientrest.model;

/**
 * Created by linwf on 2018/12/17.
 */
public class FabricConfigPeerModel {
    private int row_id;  //序号
    private String peer_name; //节点名称
    private String peer_eventhubname; //节点事件名称
    private String peer_location; //节点地址
    private String peer_eventhublocation; //节点事件地址
    private boolean is_eventlistener; //是否监听
    private int config_id; //配置ID
    private boolean is_delete; //是否删除


    public int getRow_id() {
        return this.row_id;
    }
    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }
    public void setPeer_name(String peer_name) {
        this.peer_name = peer_name;
    }
    public String getPeer_name() {
        return this.peer_name;
    }
    public void setPeer_eventhubname(String peer_eventhubname) {
        this.peer_eventhubname = peer_eventhubname;
    }
    public String getPeer_eventhubname() {
        return this.peer_eventhubname;
    }
    public void setPeer_location(String peer_location) {
        this.peer_location = peer_location;
    }
    public String getPeer_location() {
        return this.peer_location;
    }
    public void setPeer_eventhublocation(String peer_eventhublocation) {
        this.peer_eventhublocation = peer_eventhublocation;
    }
    public String getPeer_eventhublocation() {
        return this.peer_eventhublocation;
    }
    public void setIs_eventlistener(boolean is_eventlistener) {
        this.is_eventlistener = is_eventlistener;
    }
    public boolean getIs_eventlistener() {
        return this.is_eventlistener;
    }
    public void setConfig_id(int config_id) {
        this.config_id = config_id;
    }
    public int getConfig_id() {
        return this.config_id;
    }
    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }
    public boolean getIs_delete() {
        return this.is_delete;
    }
}
