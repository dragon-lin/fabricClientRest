package com.winyeahs.fabric.clientrest.model;

/**
 * Created by linwf on 2018/11/18.
 */
public class FabricConfigModel {
    private int row_id;  //序号
    private int league_id; //联盟ID
    private String org_name; //组织名称
    private String user_name; //用户名称
    private String cryptoconfig_path; //证书配置路径
    private String channelartifacts_path; //通道配置路径
    private String org_mspid; //组织msp_id
    private String org_domain; //组织域名
    private String orderer_domain; //域名
    private String channel_name; //通道
    private String chaincode_name; //智能合约名称
    private String chaincode_source; //智能合约源码
    private String chaincode_path; //智能合约路径
    private String chaincode_policy; //智能合约策略
    private String chaincode_version; //智能合约版本
    private int proposal_waittime; //提案等待时间
    private int invoke_waittime; //交易等待时间
    private boolean is_tls; //是否开启TLS
    private boolean is_catls; //是否开启CA TLS
    private boolean is_delete; //是否删除


    public int getRow_id() {
        return this.row_id;
    }
    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }
    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }
    public int getLeague_id() {
        return this.league_id;
    }
    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
    public String getOrg_name() {
        return this.org_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setCryptoconfig_path(String cryptoconfig_path) {
        this.cryptoconfig_path = cryptoconfig_path;
    }
    public String getCryptoconfig_path() {
        return this.cryptoconfig_path;
    }
    public void setChannelartifacts_path(String channelartifacts_path) {
        this.channelartifacts_path = channelartifacts_path;
    }
    public String getChannelartifacts_path() {
        return this.channelartifacts_path;
    }
    public void setOrg_mspid(String org_mspid) {
        this.org_mspid = org_mspid;
    }
    public String getOrg_mspid() {
        return this.org_mspid;
    }
    public void setOrg_domain(String org_domain) {
        this.org_domain = org_domain;
    }
    public String getOrg_domain() {
        return this.org_domain;
    }
    public void setOrderer_domain(String orderer_domain) {
        this.orderer_domain = orderer_domain;
    }
    public String getOrderer_domain() {
        return this.orderer_domain;
    }
    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
    public String getChannel_name() {
        return this.channel_name;
    }
    public void setChaincode_name(String chaincode_name) {
        this.chaincode_name = chaincode_name;
    }
    public String getChaincode_name() {
        return this.chaincode_name;
    }
    public void setChaincode_source(String chaincode_source) {
        this.chaincode_source = chaincode_source;
    }
    public String getChaincode_source() {
        return this.chaincode_source;
    }
    public void setChaincode_path(String chaincode_path) {
        this.chaincode_path = chaincode_path;
    }
    public String getChaincode_path() {
        return this.chaincode_path;
    }
    public void setChaincode_policy(String chaincode_policy) {
        this.chaincode_policy = chaincode_policy;
    }
    public String getChaincode_policy() {
        return this.chaincode_policy;
    }
    public void setChaincode_version(String chaincode_version) {
        this.chaincode_version = chaincode_version;
    }
    public String getChaincode_version() {
        return this.chaincode_version;
    }
    public void setProposal_waittime(int proposal_waittime) {
        this.proposal_waittime = proposal_waittime;
    }
    public int getProposal_waittime() {
        return this.proposal_waittime;
    }
    public void setInvoke_waittime(int invoke_waittime) {
        this.invoke_waittime = invoke_waittime;
    }
    public int getInvoke_waittime() {
        return this.invoke_waittime;
    }
    public void setIs_tls(boolean is_tls) {
        this.is_tls = is_tls;
    }
    public boolean getIs_tls() {
        return this.is_tls;
    }
    public void setIs_catls(boolean is_catls) {
        this.is_catls = is_catls;
    }
    public boolean getIs_catls() {
        return this.is_catls;
    }
    public void setIs_delete(boolean is_delete) {
        this.is_delete = is_delete;
    }
    public boolean getIs_delete() {
        return this.is_delete;
    }
}
