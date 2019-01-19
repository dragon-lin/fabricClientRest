package com.winyeahs.fabric.clientrest.model;

/**
 * Created by linwf on 2018/12/17.
 */
public class FabricConfigOrdererModel {
    private int row_id;  //序号
    private String orderer_name; //排序名称
    private String orderer_location; //排序地址
    private int config_id; //配置ID
    private int is_delete; //是否删除


    public int getRow_id() {
        return this.row_id;
    }
    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }
    public void setOrderer_name(String orderer_name) {
        this.orderer_name = orderer_name;
    }
    public String getOrderer_name() {
        return this.orderer_name;
    }
    public void setOrderer_location(String orderer_location) {
        this.orderer_location = orderer_location;
    }
    public String getOrderer_location() {
        return this.orderer_location;
    }
    public void setConfig_id(int config_id) {
        this.config_id = config_id;
    }
    public int getConfig_id() {
        return this.config_id;
    }
    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }
    public int getIs_delete() {
        return this.is_delete;
    }
}
